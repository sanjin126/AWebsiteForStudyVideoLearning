package cn.sanjin.video.common.utils;

/**
 * 用作RestController的返回类
 */
public class Result<T> {
    private int status;
    private String message;
    private T data;

    private Result(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private Result() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class Builder<T> {
        private int status;
        private String message;
        private T data;

        public Builder<T> status(int s) {
            this.status = s;
            return this;
        }

        public Builder<T> message(String m) {
            this.message = m;
            return this;
        }

        public Builder<T> data(T d) {
            this.data = d;
            return this;
        }

        public Result<T> build() {
            return new Result<>(status, message, data);
        }


    }
}
