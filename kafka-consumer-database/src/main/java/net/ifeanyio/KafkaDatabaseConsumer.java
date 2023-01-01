package net.ifeanyio;

import net.ifeanyio.entity.Wikimedia;
import net.ifeanyio.repository.WikimediaDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaDatabaseConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatabaseConsumer.class);
    private WikimediaDataRepository repository;

    public KafkaDatabaseConsumer(WikimediaDataRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
            topics = "wikimedia_recent_change",
            groupId = "wikimediaSpoolerForDb"
    )
    public void consume(String eventMessage){
        LOGGER.info(String.format("Event message received => %s", eventMessage));

        Wikimedia wikimedia = new Wikimedia();
        wikimedia.setWikiEventData(eventMessage);

        repository.save(wikimedia);
        LOGGER.info(String.format("Data Saved!"));
    }
}
