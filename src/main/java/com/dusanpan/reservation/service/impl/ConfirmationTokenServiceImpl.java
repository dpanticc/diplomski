package com.dusanpan.reservation.service.impl;
import com.dusanpan.reservation.auth.tokens.ConfirmationToken;
import com.dusanpan.reservation.repository.ConfirmationTokenRepository;
import com.dusanpan.reservation.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }
}