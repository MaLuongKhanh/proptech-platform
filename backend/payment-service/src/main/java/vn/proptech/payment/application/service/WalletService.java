package vn.proptech.payment.application.service;

import vn.proptech.payment.application.dto.input.AddWalletRequest;
import vn.proptech.payment.application.dto.input.UpdateWalletRequest;
import vn.proptech.payment.application.dto.output.GetWalletResponse;

public interface WalletService {
    
    GetWalletResponse createWallet(AddWalletRequest request);

    GetWalletResponse getWalletByUserId(String userId);
    
    GetWalletResponse updateWallet(String id, UpdateWalletRequest request);

    GetWalletResponse topUpWallet(String id, String amount);

    GetWalletResponse paymentWallet(String id, String amount);

    boolean deleteWallet(String id);
}