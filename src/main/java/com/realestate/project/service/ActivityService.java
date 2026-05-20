package com.realestate.project.service;

import com.realestate.project.model.ActivityLog;
import com.realestate.project.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ActivityService {

    private final ActivityLogRepository activityLogRepository;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public ActivityService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public ActivityLog logActivity(String activityType, String description, String icon, String colorClass) {
        ActivityLog log = new ActivityLog(activityType, description, icon, colorClass);
        ActivityLog saved = activityLogRepository.save(log);
        broadcast(saved);
        return saved;
    }

    public List<ActivityLog> getRecentActivities() {
        return activityLogRepository.findTop10ByOrderByTimestampDesc();
    }

    public SseEmitter registerEmitter() {
        SseEmitter emitter = new SseEmitter(24 * 60 * 60 * 1000L); // 24-hour timeout
        emitters.add(emitter);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));
        
        // Send initial heartbeat to establish connection successfully
        try {
            emitter.send(SseEmitter.event().name("init").data("Connected"));
        } catch (IOException e) {
            emitters.remove(emitter);
        }
        
        return emitter;
    }

    private void broadcast(ActivityLog log) {
        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("activity")
                        .data(log));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        emitters.removeAll(deadEmitters);
    }
}
