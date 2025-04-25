package com.example.springrider.domain.cart.service;

import com.example.springrider.domain.cart.dto.CartItemBulkRequestDto;
import com.example.springrider.domain.cart.dto.CartItemBulkResponseDto;
import com.example.springrider.domain.cart.dto.CartItemRequestDto;
import com.example.springrider.domain.cart.dto.CartItemSearchBulkResponseDto;
import com.example.springrider.domain.cart.dto.CartItemSearchResponseDto;
import com.example.springrider.domain.cart.dto.FailedItemDto;
import com.example.springrider.domain.cart.dto.SuccessItemDto;
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
    public CartItemBulkResponseDto addCartItems(Long userId, CartItemBulkRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException(ExceptionCode.USER_NOT_FOUND));
        Long requestStoreId = requestDto.getStoreId();
        LocalDateTime limit = LocalDateTime.now().minusDays(1);

        List<CartItem> existingCartItems = cartRepository.findAllByUserIdAndModifiedAtAfterWithMenuAndStore(
            userId, limit);
        validateExistingCart(existingCartItems, requestStoreId);

        List<SuccessItemDto> successItems = new ArrayList<>();
        List<FailedItemDto> failedItems = new ArrayList<>();

        for (CartItemRequestDto cartItemRequestDto : requestDto.getCartItems()) {
            processAddCartItem(user, requestStoreId, existingCartItems, cartItemRequestDto,
                successItems,
                failedItems);
        }
        return new CartItemBulkResponseDto(successItems, failedItems);
    }

    private void validateExistingCart(List<CartItem> existingCartItems, Long storeId) {
        if (!existingCartItems.isEmpty() && !existingCartItems.get(0).getStoreId()
            .equals(storeId)) {
            throw new InvalidRequestException(ExceptionCode.CART_STORE_MISMATCH);
        }
    }

    private void processAddCartItem(
        User user,
        Long requestStoreId,
        List<CartItem> existingCartItems,
        CartItemRequestDto cartItemRequestDto,
        List<SuccessItemDto> successItems,
        List<FailedItemDto> failedItems
    ) {
        try {
            Menu menu = menuRepository.findById(cartItemRequestDto.getMenuId())
                .orElseThrow(() -> new InvalidRequestException(ExceptionCode.MENU_NOT_FOUND));

            if (!menu.getStore().getId().equals(requestStoreId)) {
                throw new InvalidRequestException(ExceptionCode.MENU_STORE_MISMATCH);
            }

            CartItem existing = existingCartItems.stream()
                .filter(ci -> ci.getMenu().getId().equals(cartItemRequestDto.getMenuId()))
                .findFirst()
                .orElse(null);

            if (existing != null) {
                existing.updateQuantity(existing.getQuantity() + cartItemRequestDto.getQuantity());
                successItems.add(
                    new SuccessItemDto(existing.getId(), cartItemRequestDto.getMenuId(),
                        existing.getQuantity()));
            } else {
                CartItem newItem = new CartItem(user, menu, cartItemRequestDto.getQuantity());
                CartItem saved = cartRepository.save(newItem);
                successItems.add(
                    new SuccessItemDto(saved.getId(), cartItemRequestDto.getMenuId(),
                        cartItemRequestDto.getQuantity()));
            }

        } catch (InvalidRequestException e) {
            failedItems.add(
                new FailedItemDto(cartItemRequestDto.getMenuId(), e.getExceptionCode().name(),
                    e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRequestException(ExceptionCode.CART_DUPLICATE_ITEM);
        }
    }

    @Transactional(readOnly = true)
    public CartItemSearchBulkResponseDto searchCartItems(Long userId) {
        LocalDateTime limit = LocalDateTime.now().minusDays(1);
        List<CartItem> cartItems = cartRepository.findAllbyUserIdAndModifiedAtAfter(userId, limit);

        if (cartItems.isEmpty()) {
            throw new InvalidRequestException(ExceptionCode.CART_NOT_FOUND_ALL);
        }

        List<CartItemSearchResponseDto> responseDtos = cartItems.stream()
            .map(CartItemSearchResponseDto::new).toList();

        return new CartItemSearchBulkResponseDto(responseDtos, cartItems.get(0).getStoreId());
    }
}