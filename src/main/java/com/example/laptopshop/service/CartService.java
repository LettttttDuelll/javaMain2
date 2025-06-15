package com.example.laptopshop.service;

import com.example.laptopshop.dto.CartItemDto;
import com.example.laptopshop.entity.CartItem;

import com.example.laptopshop.entity.Laptop;
import com.example.laptopshop.entity.User;
import com.example.laptopshop.repository.CartItemRepository;
import com.example.laptopshop.repository.LaptopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartRepo;
    @Autowired
    private LaptopRepository laptopRepo;

    /**
     * Thêm sản phẩm vào giỏ hàng.
     * Nếu sản phẩm đã có trong giỏ, tăng số lượng (nếu không vượt quá tồn kho).
     */
    public void addToCart(User user, Long laptopId, int quantity) {
        /*Product product = productRepo.findById(productId)*/
        Laptop laptop = laptopRepo.findById(laptopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (laptop.getStock() < quantity) {
            throw new RuntimeException("Số lượng trong kho không đủ");
        }

        CartItem item = cartRepo.findByUserIdAndLaptopId(user.getId(), laptopId).orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setUser(user);
            item.setLaptop(laptop);
            item.setQuantity(quantity);
        } else {
            int newQuantity = item.getQuantity() + quantity;
            if (newQuantity > laptop.getStock()) {
                throw new RuntimeException("Tổng số lượng vượt quá tồn kho");
            }
            item.setQuantity(newQuantity);
        }
        cartRepo.save(item);
    }

    /**
     * Lấy danh sách sản phẩm trong giỏ hàng.
     */
    public List<CartItemDto> getCartDto(User user) {
        List<CartItem> cartItems = cartRepo.findByUserId(user.getId());
        return cartItems.stream().map(item -> {
            CartItemDto dto = new CartItemDto();
            dto.setId(item.getId());
            dto.setLaptopName(item.getLaptop().getName());
            dto.setImage(item.getLaptop().getImage_url());
            dto.setPrice(item.getLaptop().getCurrent_price());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).toList();
    }


    /**
     * Xóa một sản phẩm khỏi giỏ hàng.
     */
    public void removeItem(User user, Long productId) {
        CartItem item = cartRepo.findByUserIdAndLaptopId(user.getId(), productId).orElse(null);
        if (item != null) {
            cartRepo.delete(item);
        }
    }//không sài nữa

    /**
     * Cập nhật số lượng sản phẩm trong giỏ hàng.
     */
    public void updateQuantity(User user, Long laptopId, int newQuantity) {
        Laptop laptop = laptopRepo.findById(laptopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        if (newQuantity > laptop.getStock()) {
            throw new RuntimeException("Số lượng vượt quá tồn kho");
        }
        CartItem item = cartRepo.findByUserIdAndLaptopId(user.getId(), laptopId).orElse(null);
        if (item == null) throw new RuntimeException("Sản phẩm không có trong giỏ hàng");
        if (newQuantity <= 0) {
            cartRepo.delete(item);
        } else {
            item.setQuantity(newQuantity);
            cartRepo.save(item);
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng sau khi đặt hàng thành công.
     */
    public void clearCart(User user) {
        cartRepo.deleteByUserId(user.getId());
    }

    public void removeItemById(Integer cartItemId) {
        cartRepo.deleteById(cartItemId);
    }

}


