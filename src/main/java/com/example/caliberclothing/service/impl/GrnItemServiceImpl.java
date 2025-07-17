package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.GrnItemDTO;
import com.example.caliberclothing.entity.Grn;
import com.example.caliberclothing.entity.GrnItem;
import com.example.caliberclothing.entity.Product;
import com.example.caliberclothing.repository.GrnItemRepository;
import com.example.caliberclothing.service.GrnItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GrnItemServiceImpl implements GrnItemService {

    @Autowired
    private GrnItemRepository grnItemRepository;

    public List<GrnItem> getAllGrnItems() {
        return grnItemRepository.findAll();
    }

    public Optional<GrnItem> getGrnItemById(Integer id) {
        return grnItemRepository.findById(id);
    }

    public GrnItem createGrnItem(GrnItemDTO grnItemDTO) {
        GrnItem grnItem = GrnItem.builder()
                .grn(Grn.builder().id(grnItemDTO.getGrn().getId()).build())
                .product(Product.builder().id(grnItemDTO.getProduct().getId()).build())
                .quantityReceived(grnItemDTO.getQuantityReceived())
                .unitCost(grnItemDTO.getUnitCost())
                .subTotal(grnItemDTO.getSubTotal())
                .build();

        return grnItemRepository.save(grnItem);
    }

    public GrnItem updateGrnItem(Integer id, GrnItemDTO grnItemDTO) {
        Optional<GrnItem> existingGrnItem = grnItemRepository.findById(id);
        if (existingGrnItem.isPresent()) {
            GrnItem grnItem = existingGrnItem.get();
            grnItem.setGrn(Grn.builder().id(grnItemDTO.getGrn().getId()).build());
            grnItem.setProduct(Product.builder().id(grnItemDTO.getProduct().getId()).build());
            grnItem.setQuantityReceived(grnItemDTO.getQuantityReceived());
            grnItem.setUnitCost(grnItemDTO.getUnitCost());
            grnItem.setSubTotal(grnItemDTO.getSubTotal());

            return grnItemRepository.save(grnItem);
        }
        throw new RuntimeException("GRN Item not found with id: " + id);
    }

    public void deleteGrnItem(Integer id) {
        grnItemRepository.deleteById(id);
    }

    public List<GrnItem> getGrnItemsByGrnId(Integer grnId) {
        return grnItemRepository.findByGrnId(grnId);
    }

    public List<GrnItem> getGrnItemsByProductId(Integer productId) {
        return grnItemRepository.findByProductId(productId);
    }
}
