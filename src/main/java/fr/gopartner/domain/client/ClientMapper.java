package fr.gopartner.domain.client;

import org.springframework.stereotype.Service;

@Service
public class ClientMapper {

    public Client toEntity(ClientDto clientDto) {
        if (clientDto == null) {
            return null;
        }
        return Client.builder()
                .name(clientDto.getName())
                .address(clientDto.getAddress())
                .email(clientDto.getEmail())
                .build();
    }
}
