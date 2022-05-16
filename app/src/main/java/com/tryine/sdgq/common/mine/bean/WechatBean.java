package com.tryine.sdgq.common.mine.bean;

public class WechatBean {

    /**
     * code : 200
     * message : 操作成功!
     * result : {"packageValue":"Sign=WXPay","appid":"wx764b087456886f63","sign":"1A012F52F322D0986C2637098069DB97","partnerid":"1590798761","prepayid":"wx0215471834972315fd406a65c280f10000","noncestr":"45509071e9c241d1834db980b42a602a","timestamp":"1627890501"}
     */

    private String code;
    private String message;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * packageValue : Sign=WXPay
         * appid : wx764b087456886f63
         * sign : 1A012F52F322D0986C2637098069DB97
         * partnerid : 1590798761
         * prepayid : wx0215471834972315fd406a65c280f10000
         * noncestr : 45509071e9c241d1834db980b42a602a
         * timestamp : 1627890501
         */

        private String packageValue;
        private String appid;
        private String sign;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;

        public String getPackageValue() {
            return packageValue;
        }

        public void setPackageValue(String packageValue) {
            this.packageValue = packageValue;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
