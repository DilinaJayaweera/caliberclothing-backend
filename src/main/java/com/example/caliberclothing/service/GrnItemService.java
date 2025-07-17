package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.GrnItemDTO;
import com.example.caliberclothing.entity.Grn;
import com.example.caliberclothing.entity.GrnItem;
import com.example.caliberclothing.entity.Product;

import java.util.List;
import java.util.Optional;

public interface GrnItemService {

    public List<GrnItem> getAllGrnItems();

    public Optional<GrnItem> getGrnItemById(Integer id);

    public GrnItem createGrnItem(GrnItemDTO grnItemDTO);

    public GrnItem updateGrnItem(Integer id, GrnItemDTO grnItemDTO);

    public void deleteGrnItem(Integer id);

    public List<GrnItem> getGrnItemsByGrnId(Integer grnId);

    public List<GrnItem> getGrnItemsByProductId(Integer productId);

}
