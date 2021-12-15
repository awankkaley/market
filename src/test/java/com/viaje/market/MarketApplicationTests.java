package com.viaje.market;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.viaje.market.util.HmacValidator;
import com.viaje.market.util.RestUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles(profiles = "local")
@RunWith(SpringRunner.class)
class MarketApplicationTests {

    Logger logger = LoggerFactory.getLogger("MarketTest");
//    String BASE_URL = "http://viajemarketapidevenv1-env.ap-southeast-1.elasticbeanstalk.com";
    String BASE_URL = "http://localhost:8080";
    String SECRET = "ux1hv8k3iyqcnp4d0ddm9jn2vpgdv4ptzwkeoykep67ig87rri";
    String API_KEY = "46sfy6etc12400cg1uxn58oexi0y0uffgn587y23ncganfqig9";
    RestUtil restUtil = new RestUtil(new RestTemplateBuilder());

    @Test
    void checkBalanceCoinsbit() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/balance/coinsbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=coinsbit";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonNode getRes = restUtil.getRequestWithToken(url, signature);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("result").get("exchange").textValue()).isEqualTo("Coinsbit");
    }

    @Test
    void checkBalanceHotbitt() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/balance/hotbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=hotbit";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonNode getRes = restUtil.getRequestWithToken(url, signature);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("result").get("exchange").textValue()).isEqualTo("Hotbit");
    }

    @Test
    void marketTodayCoinsbit() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/market/today/coinsbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=coinsbit";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonNode getRes = restUtil.getRequestWithToken(url, signature);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("result").get("exchange").textValue()).isEqualTo("Coinsbit");
    }

    @Test
    void marketTodayHotbit() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/market/today/hotbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=hotbit";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonNode getRes = restUtil.getRequestWithToken(url, signature);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("result").get("exchange").textValue()).isEqualTo("Hotbit");
    }

    @Test
    void marketTodayByPeriodHotbit() throws JsonProcessingException {
        String periode = "84000";
        String url = BASE_URL + "/api/v1/market/period?period=" + periode;

        String payload = "x-api-key=" + API_KEY + "&period=" + periode;
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonNode getRes = restUtil.getRequestWithToken(url, signature);

        logger.info("get res::{}", getRes);

        assertThat(String.valueOf(getRes.get("result").get("period").intValue())).isEqualTo(periode);
    }


    @Test
    void buyCoinsbit() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/order/single/coinsbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=coinsbit" + "&side=buy" + "&amount=1.0";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonObject postBody = new JsonObject();
        postBody.addProperty("side", "buy");
        postBody.addProperty("amount", 1.0);

        JsonNode getRes = restUtil.postWithBody(url, signature, postBody);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("result").get("exchangeCode").textValue()).isEqualTo("coinsbit");
    }

    @Test
    void sellCoinsbit() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/order/single/coinsbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=coinsbit" + "&side=sell" + "&amount=1.0";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonObject postBody = new JsonObject();
        postBody.addProperty("side", "sell");
        postBody.addProperty("amount", 1.0);

        JsonNode getRes = restUtil.postWithBody(url, signature, postBody);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("result").get("exchangeCode").textValue()).isEqualTo("coinsbit");
    }

    @Test
    void buyHotbit() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/order/single/hotbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=hotbit" + "&side=buy" + "&amount=1.0";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonObject postBody = new JsonObject();
        postBody.addProperty("side", "buy");
        postBody.addProperty("amount", 1.0);

        JsonNode getRes = restUtil.postWithBody(url, signature, postBody);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("result").get("exchangeCode").textValue()).isEqualTo("hotbit");
    }

    @Test
    void sellHotbit() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/order/single/hotbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=hotbit" + "&side=sell" + "&amount=1.0";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonObject postBody = new JsonObject();
        postBody.addProperty("side", "sell");
        postBody.addProperty("amount", 1.0);

        JsonNode getRes = restUtil.postWithBody(url, signature, postBody);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("result").get("exchangeCode").textValue()).isEqualTo("hotbit");
    }

    @Test
    void twoSideCoinsbit() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/order/both/coinsbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=coinsbit" ;
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonObject postBody = new JsonObject();
        postBody.addProperty("amount", 2);
        postBody.addProperty("buyPercent", 50);
        postBody.addProperty("profitPercent", 1);

        JsonNode getRes = restUtil.postWithBody(url, signature, postBody);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("error").textValue()).isEqualTo(null);
    }

    @Test
    void twoSideHotbit() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/order/both/hotbit";

        String payload = "x-api-key=" + API_KEY + "&exchange=hotbit" ;
        String signature = HmacValidator.generateSignature(SECRET, payload);
        logger.debug(signature);
        JsonObject postBody = new JsonObject();
        postBody.addProperty("amount", 2);
        postBody.addProperty("buyPercent", 50);
        postBody.addProperty("profitPercent", 1);


        JsonNode getRes = restUtil.postWithBody(url, signature, postBody);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("error").textValue()).isEqualTo(null);
    }


    @Test
    void getAllOrder() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/all/0/100";

        String payload = "x-api-key=" + API_KEY + "&page=0&limit=100";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonNode getRes = restUtil.getRequestWithToken(url, signature);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("error").textValue()).isEqualTo(null);

    }

    @Test
    void getAllOrderStatus() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/by_status/0/100/1";

        String payload = "x-api-key=" + API_KEY + "&page=0&limit=100&status=1";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonNode getRes = restUtil.getRequestWithToken(url, signature);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("error").textValue()).isEqualTo(null);

    }

    @Test
    void getOrderById() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/by_id/3";

        String payload = "x-api-key=" + API_KEY + "&orderId=3";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonNode getRes = restUtil.getRequestWithToken(url, signature);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("error").textValue()).isEqualTo(null);

    }

    @Test
    void getOrderDetailById() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/detail/3";

        String payload = "x-api-key=" + API_KEY + "&orderId=3";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonNode getRes = restUtil.getRequestWithToken(url, signature);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("error").textValue()).isEqualTo(null);

    }

    @Test
    void cancelOrder() throws JsonProcessingException {
        String url = BASE_URL + "/api/v1/book/cancel";

        String payload = "x-api-key=" + API_KEY + "&orderId=3";
        String signature = HmacValidator.generateSignature(SECRET, payload);

        JsonObject postBody = new JsonObject();
        postBody.addProperty("orderId", "3");

        JsonNode getRes = restUtil.postWithBody(url, signature, postBody);

        logger.info("get res::{}", getRes);

        assertThat(getRes.get("error").textValue()).isEqualTo(null);

    }


}
