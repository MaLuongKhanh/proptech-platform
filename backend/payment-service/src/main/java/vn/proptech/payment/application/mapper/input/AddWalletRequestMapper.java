package vn.proptech.payment.application.mapper.input;

import vn.proptech.payment.application.dto.input.AddWalletRequest;
import vn.proptech.payment.domain.model.Wallet;

public class AddWalletRequestMapper {
    public static Wallet AddWalletMapDTOToEntity(AddWalletRequest request, String id) {
        return Wallet.builder()
                .id(id)
                .userId(request.getUserId())
                .balance(request.getBalance())
                .currency(request.getCurrency())
                .build();
    }
}