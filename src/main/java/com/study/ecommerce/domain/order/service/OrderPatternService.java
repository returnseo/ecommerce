package com.study.ecommerce.domain.order.service;

import com.study.ecommerce.domain.order.template.PremiumOrderProcessor;
import com.study.ecommerce.domain.order.template.RegularOrderProcessor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderPatternService {
    private final RegularOrderProcessor regularOrderProcessor;
    private final PremiumOrderProcessor premiumOrderProcessor;
    private final ApplicationEventPublisher eventPublisher;


}
