package com.spring4all.minipay.wxpay.dao;

import com.spring4all.minipay.wxpay.entity.WXNotification;
import org.springframework.data.repository.CrudRepository;

public interface WXNotificationRepository
        extends CrudRepository<WXNotification, Long> {


}
