package com.easy_p.easyp.service;

import com.easy_p.easyp.dto.response.AlarmDto;

public interface AlarmService {
    void sendAlarm(String targetEmail, AlarmDto alarmDto);
}
