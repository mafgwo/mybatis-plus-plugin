package path.objs;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 */
@Builder
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private T data;

    @Getter
    @Setter
    private String msg = CommonConstants.SUCCESS_MSG;

    @Getter
    @Setter
    private int code = CommonConstants.SUCCESS_CODE;

    public Result(T data) {
        this.data = data;
    }

    public Result(T data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    public Result(Throwable e) {
        this.msg = e.getMessage();
        this.code = CommonConstants.FAIL_CODE;
    }
}
