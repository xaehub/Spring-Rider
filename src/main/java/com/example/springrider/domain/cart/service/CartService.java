package com.example.springrider.domain.cart.service;

import com.example.springrider.domain.cart.dto.CreationCartItemBulkRequestDto;
import com.example.springrider.domain.cart.dto.CreationCartItemBulkResponseDto;
import com.example.springrider.domain.cart.dto.CreationCartItemRequestDto;
import com.example.springrider.domain.cart.dto.FailedItemDto;
import com.example.springrider.domain.cart.dto.FindCartItemBulkResponseDto;
import com.example.springrider.domain.cart.dto.FindCartItemResponseDto;
import com.example.springrider.domain.cart.dto.SuccessItemDto;
import com.example.springrider.domain.cart.dto.UpdateCartItemRequestDto;
import com.example.springrider.domain.cart.dto.UpdateCartItemResponseDto;
import com.example.springrider.domain.cart.entity.CartItem;
import com.example.springrider.domain.cart.repository.CartRepository;
import com.example.springrider.domain.common.exception.ExceptionCode;
import com.example.springrider.domain.common.exception.InvalidRequestException;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.menu.repository.MenuRepository;
import com.example.springrider.domain.user.entity.User;
import com.example.springrider.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreationCartItemBulkResponseDto create(Long userId,
        CreationCartItemBulkRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.USER_NOT_FOUND));
        Long requestStoreId = requestDto.getStoreId();
        LocalDateTime limit = LocalDateTime.now().minusDays(1);

        List<CartItem> existingCartItems = cartRepository.findAllByUserIdAndModifiedAtAfterWithMenuAndStore(
            userId, limit);
        validateExistingCart(existingCartItems, requestStoreId);

        List<SuccessItemDto> successItems = new ArrayList<>();
        List<FailedItemDto> failedItems = new ArrayList<>();

        for (CreationCartItemRequestDto creationCartItemRequestDto : requestDto.getCartItems()) {
            processCreation(user, requestStoreId, existingCartItems, creationCartItemRequestDto,
                successItems,
                failedItems);
        }
        return new CreationCartItemBulkResponseDto(successItems, failedItems);
    }

    private void validateExistingCart(List<CartItem> existingCartItems, Long storeId) {
        if (!existingCartItems.isEmpty() && !existingCartItems.get(0).getStoreId()
            .equals(storeId)) {
            throw new InvalidRequestException(ExceptionCode.CART_STORE_MISMATCH);
        }
    }

    private void processCreation(
        User user,
        Long requestStoreId,
        List<CartItem> existingCartItems,
        CreationCartItemRequestDto creationCartItemRequestDto,
        List<SuccessItemDto> successItems,
        List<FailedItemDto> failedItems
    ) {
        try {
            Menu menu = menuRepository.findById(creationCartItemRequestDto.getMenuId())
                .orElseThrow(() -> new InvalidRequestException(ExceptionCode.MENU_NOT_FOUND));

            if (!menu.getStore().getId().equals(requestStoreId)) {
                throw new InvalidRequestException(ExceptionCode.MENU_STORE_MISMATCH);
            }

            CartItem existing = existingCartItems.stream()
                .filter(ci -> ci.getMenu().getId().equals(creationCartItemRequestDto.getMenuId()))
                .findFirst()
                .orElse(null);

            if (existing != null) {
                existing.updateQuantity(
                    existing.getQuantity() + creationCartItemRequestDto.getQuantity());
                successItems.add(
                    new SuccessItemDto(existing.getId(), creationCartItemRequestDto.getMenuId(),
                        existing.getQuantity()));
            } else {
                CartItem newItem = new CartItem(user, menu,
                    creationCartItemRequestDto.getQuantity());
                CartItem saved = cartRepository.save(newItem);
                successItems.add(
                    new SuccessItemDto(saved.getId(), creationCartItemRequestDto.getMenuId(),
                        creationCartItemRequestDto.getQuantity()));
            }

        } catch (InvalidRequestException e) {
            failedItems.add(
                new FailedItemDto(creationCartItemRequestDto.getMenuId(),
                    e.getExceptionCode().name(),
                    e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRequestException(ExceptionCode.CART_DUPLICATE_ITEM);
        }
    }

    @Transactional(readOnly = true)
    public FindCartItemBulkResponseDto findAll(Long userId) {
        LocalDateTime limit = LocalDateTime.now().minusDays(1);
        List<CartItem> cartItems = cartRepository.findAllbyUserIdAndModifiedAtAfterWithMenu(userId,
            limit);

        if (cartItems.isEmpty()) {
            throw new InvalidRequestException(ExceptionCode.CART_NOT_FOUND_ALL);
        }

        List<FindCartItemResponseDto> responseDtos = cartItems.stream()
            .map(FindCartItemResponseDto::new).toList();
        int sumTotalprice = sumTotalprice(responseDtos);

        return FindCartItemBulkResponseDto.toDto(responseDtos, cartItems.get(0).getStoreId(),
            sumTotalprice);
    }

    public int sumTotalprice(List<FindCartItemResponseDto> responseDtos) {
        int sum = responseDtos.stream().mapToInt(d -> d.getPrice() * d.getQuantity()
        ).sum();
        return sum;
    }

    @Transactional
    public UpdateCartItemResponseDto update(
        Long cartItemId, UpdateCartItemRequestDto requestDto, Long userId) {
        CartItem cartItem = cartRepository.findByIdWithUser(cartItemId)
            .orElseThrow(() -> new InvalidRequestException(
                ExceptionCode.CART_NOT_FOUND));
        if (!cartItem.getUser().getId().equals(userId)) {
            throw new InvalidRequestException(ExceptionCode.AUTH_EXCEPTION);
        }
        if (isValidQuantity(requestDto) && !hasStatus(requestDto)) {
            cartItem.updateQuantity(requestDto.getQuantity());
        }
        if (isValidQuantity(requestDto) && hasStatus(requestDto)) {
            cartItem.changeStatus(requestDto.getStatus());
        }
        if (isValidQuantity(requestDto) && hasStatus(requestDto)) {
            cartItem.updateQuantity(requestDto.getQuantity());
            cartItem.changeStatus(requestDto.getStatus());
        }
        return UpdateCartItemResponseDto.toDto(cartItem);
    }

    public boolean isValidQuantity(UpdateCartItemRequestDto requestDto) {
        Integer quantity = requestDto.getQuantity();
        return quantity != null && quantity > 0;
    }

    public boolean hasStatus(UpdateCartItemRequestDto requestDto) {
        return requestDto.getStatus() != null;
    }
}