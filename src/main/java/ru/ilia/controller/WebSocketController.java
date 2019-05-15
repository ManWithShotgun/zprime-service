package ru.ilia.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.ilia.controller.entities.WsOnePointRequest;
import ru.ilia.controller.entities.WsOnePointResponse;
import ru.ilia.controller.entities.WsPointsRequest;
import ru.ilia.controller.entities.WsPointsResponse;
import ru.ilia.services.ZprimeService;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
public class WebSocketController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ZprimeService zprimeService;

    @Autowired
    WebSocketController(SimpMessagingTemplate template, SimpMessageSendingOperations messagingTemplate, ZprimeService zprimeService) {
        this.messagingTemplate = messagingTemplate;
        this.zprimeService = zprimeService;
    }

    @MessageMapping("/send/message")
    @SendToUser("/chat/reply")
    public String onReceivedMesage(@Payload String message) {
//        this.template.convertAndSend("/chat",  new SimpleDateFormat("HH:mm:ss").format(new Date())+"- "+message);
        return "rrr: " + message;
    }

    @MessageMapping("/get-data")
    @SendToUser("/data/reply")
    @ResponseBody
    public ResponseEntity<?> onGetResult(@Valid @RequestBody WsOnePointRequest request) {
        try {
            String ksi = request.getKsi();
            String mass = request.getMass();
            log.info("Cache: startCache");
            String result = zprimeService.getResult(ksi, mass, request.getEvents(), request.getPointCalculationQuantity());
            log.info("Cache: endCache");
            // create response
            return ResponseEntity.ok(new WsOnePointResponse(ksi, mass, result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @MessageMapping("/get-data-all")
    @SendToUser("/data-all/reply")
    @ResponseBody
    public ResponseEntity<?> onGetAllResults(@Valid @RequestBody WsPointsRequest request) {
        try {
            String ksi = request.getKsi();
            Map<String, String> result = zprimeService.getAllResults(ksi);
            return ResponseEntity.ok(new WsPointsResponse(ksi, result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
