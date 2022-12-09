package fr.gopartner.core.batch;

import fr.gopartner.domain.client.Client;
import fr.gopartner.domain.client.ClientRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientItemWriter implements ItemWriter<Client> {

    private final ClientRepository clientRepository;

    public ClientItemWriter(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void write(List<? extends Client> list) throws Exception {
        list.forEach(client -> {
            clientRepository.save(client);
        });
    }
}

