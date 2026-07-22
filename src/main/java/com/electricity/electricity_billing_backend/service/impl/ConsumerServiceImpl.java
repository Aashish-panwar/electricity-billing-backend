package com.electricity.electricity_billing_backend.service.impl;
import org.springframework.cache.annotation.CacheEvict;
import com.electricity.electricity_billing_backend.dto.request.ConsumerRequest;
import com.electricity.electricity_billing_backend.dto.response.ConsumerResponse;
import com.electricity.electricity_billing_backend.entity.Consumer;
import com.electricity.electricity_billing_backend.entity.User;
import com.electricity.electricity_billing_backend.exception.DuplicateResourceException;
import com.electricity.electricity_billing_backend.exception.ResourceNotFoundException;
import com.electricity.electricity_billing_backend.mapper.ConsumerMapper;
import com.electricity.electricity_billing_backend.repository.ConsumerRepository;
import com.electricity.electricity_billing_backend.repository.UserRepository;
import com.electricity.electricity_billing_backend.service.ConsumerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ConsumerServiceImpl implements ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final UserRepository userRepository;
    private final ConsumerMapper consumerMapper;

    @Override
    @CacheEvict(value = "statistics", allEntries = true)
    public ConsumerResponse createConsumer(ConsumerRequest request) {

        if (consumerRepository.existsByConsumerNumber(request.getConsumerNumber())) {
            throw new DuplicateResourceException("Consumer number already exists.");
        }

        if (consumerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        Consumer consumer = Consumer.builder()
                .consumerNumber(request.getConsumerNumber())
                .fullName(request.getFullName())
                .mobileNumber(request.getMobileNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pinCode(request.getPinCode())
                .consumerType(request.getConsumerType())
                .user(user)
                .build();

        Consumer savedConsumer = consumerRepository.save(consumer);

        log.info("Consumer created: {}", savedConsumer.getConsumerNumber());

        return consumerMapper.toResponse(savedConsumer);
    }

    @Override
    public ConsumerResponse getConsumerById(Long id) {

        Consumer consumer = consumerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found."));

        return consumerMapper.toResponse(consumer);
    }

    @Override
    public List<ConsumerResponse> getAllConsumers() {

        return consumerRepository.findAll()
                .stream()
                .filter(Consumer::getActive)
                .map(consumerMapper::toResponse)
                .toList();
    }

    @Override
    @CacheEvict(value = "statistics", allEntries = true)
    public ConsumerResponse updateConsumer(Long id, ConsumerRequest request) {

        Consumer consumer = consumerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Consumer not found."));

        consumer.setFullName(request.getFullName());
        consumer.setMobileNumber(request.getMobileNumber());
        consumer.setEmail(request.getEmail());
        consumer.setAddress(request.getAddress());
        consumer.setCity(request.getCity());
        consumer.setState(request.getState());
        consumer.setPinCode(request.getPinCode());
        consumer.setConsumerType(request.getConsumerType());

        Consumer updatedConsumer = consumerRepository.save(consumer);

        log.info("Consumer updated: {}", updatedConsumer.getConsumerNumber());

        return consumerMapper.toResponse(updatedConsumer);
    }

    @CacheEvict(value = "statistics", allEntries = true)
    @Override
    public void deleteConsumer(Long id) {

        Consumer consumer = consumerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Consumer not found."));

        consumer.setActive(false);

        consumerRepository.save(consumer);

        log.warn("Consumer deleted: {}", consumer.getConsumerNumber());
    }

    @Override
    public Page<ConsumerResponse> getAllConsumers(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return consumerRepository
                .findByActiveTrue(pageable)
                .map(consumerMapper::toResponse);
    }
    @Override
    public Page<ConsumerResponse> searchConsumers(
            String keyword,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return consumerRepository
                .search(keyword, pageable)
                .map(consumerMapper::toResponse);
    }
}