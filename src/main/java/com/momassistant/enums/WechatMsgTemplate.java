package com.momassistant.enums;

/**
 * Created by zhufeng on 2018/8/19.
 */
public enum WechatMsgTemplate {
    GESTATION_MSG("cLOx2AKaw9ithTcEx_27M9I-BcuZBh8bttfVoH3Lb_k", new String[]{"first", "keyword1", "keyword2", "remark"}),
    LACTATION_MSG("5jibBFNMpZDWNWW5221Jw1YBGahb5ssGiMg9w0K_fe0", new String[]{"first", "keyword1", "keyword2", "keyword3", "keyword4", "remark"}),;
    WechatMsgTemplate(String templateId, String[] keywords) {
        this.templateId = templateId;
        this.keywords = keywords;
    }

    private String templateId;
    private String[] keywords;

    public String getTemplateId() {
        return templateId;
    }

    public String[] getKeywords() {
        return keywords;
    }
}
