package vn.proptech.payment.domain.repository;

import org.springframework.stereotype.Repository;
import vn.proptech.payment.domain.model.Wallet;

@Repository
public interface WalletRepository {
    
    Wallet findById(String id);
    Wallet findByUserId(String userId);
    <S extends Wallet> S save(S entity);
}