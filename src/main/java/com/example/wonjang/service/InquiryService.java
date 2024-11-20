package com.example.wonjang.service;

import com.example.wonjang.model.Inquiry;
import com.example.wonjang.repository.InquiryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InquiryService {
    private final InquiryRepository inquiryRepository;

    public InquiryService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    public void save(Inquiry inquiry) {
        inquiryRepository.save(inquiry);
    }
}
