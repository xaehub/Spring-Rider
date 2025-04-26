package com.example.springrider.domain.cart.service;

import com.example.springrider.domain.cart.dto.CreateCartItemBulkRequestDto;
import com.example.springrider.domain.cart.dto.CreateCartItemBulkResponseDto;
import com.example.springrider.domain.cart.dto.CreateCartItemRequestDto;
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
    public CreateCartItemBulkResponseDto create(Long userId,
        CreateCartItemBulkRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.USER_NOT_FOUND));
        Long requestStoreId = requestDto.getStoreId();
        LocalDateTime limit = LocalDateTime.now().minusDays(1);

        List<CartItem> existingCartItems = cartRepository.findAllByUserIdAndModifiedAtAfterWithMenuAndStore(
            userId, limit);
        validateExistingCart(existingCartItems, requestStoreId);

        List<SuccessItemDto> successItems = new ArrayList<>();
        List<FailedItemDto> failedItems = new ArrayList<>();

        for (CreateCartItemRequestDto createCartItemRequestDto : requestDto.getCartItems()) {
            processCreation(user, requestStoreId, existingCartItems, createCartItemRequestDto,
                successItems,
                failedItems);
        }
        return new CreateCartItemBulkResponseDto(successItems, failedItems);
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
        CreateCartItemRequestDto createCartItemRequestDto,
        List<SuccessItemDto> successItems,
        List<FailedItemDto> failedItems
    ) {
        try {
            Menu menu = menuRepository.findById(createCartItemRequestDto.getMenuId())
                .orElseThrow(() -> new InvalidRequestException(ExceptionCode.MENU_NOT_FOUND));

            if (!menu.getStore().getId().equals(requestStoreId)) {
                throw new InvalidRequestException(ExceptionCode.MENU_STORE_MISMATCH);
            }

            CartItem existing = existingCartItems.stream()
                .filter(ci -> ci.getMenu().getId().equals(createCartItemRequestDto.getMenuId()))
                .findFirst()
                .orElse(null);

            if (existing != null) {
                existing.updateQuantity(
                    existing.getQuantity() + createCartItemRequestDto.getQuantity());
                successItems.add(
                    new SuccessItemDto(existing.getId(), createCartItemRequestDto.getMenuId(),
                        existing.getQuantity()));
            } else {
                CartItem newItem = new CartItem(user, menu,
                    createCartItemRequestDto.getQuantity());
                CartItem saved = cartRepository.save(newItem);
                successItems.add(
                    new SuccessItemDto(saved.getId(), createCartItemRequestDto.getMenuId(),
                        createCartItemRequestDto.getQuantity()));
            }

        } catch (InvalidRequestException e) {
            failedItems.add(
                new FailedItemDto(createCartItemRequestDto.getMenuId(),
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

        return new FindCartItemBulkResponseDto(responseDtos, cartItems.get(0).getStoreId());
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
        if (requestDto.isValidQuantity() && !requestDto.hasStatus()) {
            cartItem.updateQuantity(requestDto.getQuantity());
        }
        if (!requestDto.isValidQuantity() && requestDto.hasStatus()) {
            cartItem.changeStatus(requestDto.getStatus());
        }
        if (requestDto.isValidQuantity() && requestDto.hasStatus()) {
            cartItem.updateQuantity(requestDto.getQuantity());
            cartItem.changeStatus(requestDto.getStatus());
        }
        return UpdateCartItemResponseDto.toDto(cartItem);
    }
}