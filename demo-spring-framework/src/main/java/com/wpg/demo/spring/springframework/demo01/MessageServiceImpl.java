package com.wpg.demo.spring.springframework.demo01;

import org.springframework.stereotype.Service;

/**
 * @author ChangWei Li
 * @version 2017-08-25 09:28
 */
@Service
class MessageServiceImpl implements MessageService {

    @Override
    public String getMessage() {
        return getClass().getCanonicalName();
    }
}
