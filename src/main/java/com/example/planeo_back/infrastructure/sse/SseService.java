package com.example.planeo_back.infrastructure.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SseService {
    private static final Logger log = LoggerFactory.getLogger(SseService.class);
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public SseService() {
        scheduler.scheduleAtFixedRate(this::sendHeartbeats, 25, 25, TimeUnit.SECONDS);
    }

    public SseEmitter subscribe(String username) {
        log.info("Subscribe appelé pour {}", username);
        log.info("Emitters actuels : {}", emitters.keySet());

        SseEmitter existing = emitters.remove(username); // atomique — remove avant création
        if (existing != null) {
            log.info("Ancien emitter trouvé pour {}, fermeture", username);
            try { existing.complete(); } catch (Exception ignored) {}
        }

        SseEmitter emitter = new SseEmitter(Duration.ofHours(1).toMillis());

        emitter.onCompletion(() -> {
            log.info("onCompletion déclenché pour {}", username);
            emitters.remove(username, emitter); // ← clé + valeur
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout déclenché pour {}", username);
            emitters.remove(username, emitter);
        });
        emitter.onError(throwable -> {
            log.info("onError déclenché pour {} : {}", username, throwable.getMessage());
            emitters.remove(username, emitter);
        });

        emitters.put(username, emitter);
        log.info("Emitter ajouté pour {}. Map contient : {}", username, emitters.keySet());
        return emitter;
    }

    public void send(String username, EventName eventName, Object data) {
        SseEmitter emitter = emitters.get(username);
        log.info("Sender appelé avec {}", username);
        log.info("Emmiter avec {}", emitter);
        if (emitter == null) return;
        try {
            emitter.send(SseEmitter.event().name(eventName.label).data(data));
        } catch (IOException e) {
            log.error("Client SSE déconnecté pour {}, nettoyage de l'emitter", username);
            emitters.remove(username);
            emitter.completeWithError(e);
        } catch (Exception e) {
            log.error("Erreur inattendue lors de l'envoi SSE pour {}", username, e);
            emitters.remove(username);
            emitter.completeWithError(e);
        }
    }

    private void sendHeartbeats() {
        emitters.forEach((username, emitter) -> {
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
            } catch (Exception e) {
                log.error("Heartbeat échoué pour {}, emitter supprimé", username);
                emitters.remove(username);
            }
        });
    }
}