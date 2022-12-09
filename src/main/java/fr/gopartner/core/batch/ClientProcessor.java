package fr.gopartner.core.batch;

import fr.gopartner.domain.client.Client;
import fr.gopartner.domain.client.ClientDto;
import fr.gopartner.domain.client.ClientMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClientProcessor implements ItemProcessor<ClientDto, Client> {

    private final ClientMapper clientMapper;

    public ClientProcessor(ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
    }

    @Override
    public Client process(ClientDto clientDto) throws Exception {
        log.info("processing the Sales: {}", clientDto.getName().toUpperCase());
        clientDto.setName(clientDto.getName().toUpperCase());
        return clientMapper.toEntity(clientDto);
    }


}
