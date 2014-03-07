package org.jetbrains.codeGolf.plugin;

import jet.JetObject;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetValueParameter;

@JetClass(signature = "Ljava/lang/Object;", abiVersion = 6)
public final class GolfResult
        implements JetObject {
    private Integer result;
    private Integer bestResult;
    private String url;
    private String errorMessage;

    public Integer getBestResult() {
        return bestResult;
    }

    public void setBestResult(Integer bestResult) {
        this.bestResult = bestResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GolfResult(Integer result, Integer bestResult, String url, String errorMessage) {
        this.result = result;
        this.bestResult = bestResult;
        this.url = url;
        this.errorMessage = errorMessage;
    }

    public GolfResult() {
    }
}