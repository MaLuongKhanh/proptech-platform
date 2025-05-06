package vn.proptech.payment.application.mapper.output;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import vn.proptech.payment.application.dto.output.GetWalletResponse;
import vn.proptech.payment.domain.model.Wallet;

@Component
public class GetWalletResponseMapper {
    public static GetWalletResponse GetWalletMapEntityToDTO(Wallet wallet) {
        return GetWalletResponse.builder()
            .id(wallet.getId())
            .userId(wallet.getUserId())
            .balance(wallet.getBalance())
            .currency(wallet.getCurrency())
            .build();
    }

    public static List<GetWalletResponse> GetWalletMapEntityToDTO(List<Wallet> contracts) {
        return contracts.stream()
            .map(GetWalletResponseMapper::GetWalletMapEntityToDTO)
            .collect(Collectors.toList());
    }
}