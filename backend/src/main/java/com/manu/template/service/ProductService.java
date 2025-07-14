package com.manu.template.service;

import com.manu.template.dto.ProductDTO;
import com.manu.template.mapper.ProductMapper;
import com.manu.template.model.Product;
import com.manu.template.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;


    public List<ProductDTO> findAll() {
        return repository.findAll().stream().map(ProductMapper::toDto).toList();
    }

    public Optional<ProductDTO> findById(Long id) {
        return repository.findById(id).map(ProductMapper::toDto);
    }

    public ProductDTO save(ProductDTO dto) {
        Product saved = repository.save(ProductMapper.toEntity(dto));
        return ProductMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}