package youngScholar.model;

import lombok.Data;
import lombok.Getter;

@Data
public class Output<T>
{
    private int code;
    private Code codeInfo;
    private T data;

    public Output(Code code, T data)
    {
        this.codeInfo = code;
//        this.code = code.getCode();
        this.data = data;
    }

    public enum Code
    {
        OK(0),
        NotRegister(100),
        Error(200),
        NotLogin(201),
        MaxSessions(202),
        ParameterError(203),
        UserExists(204),
        NotBelong(205),
        MissionStatusError(206),
        MaxCount(207),
        InsufficientBalance(208),
        NotAuth(209),;

        @Getter
        private final int code;

        Code(int code)
        {
            this.code = code;
        }
    }

    public static <T> Output<T> output(Output.Code code)
    {
        return new Output<>(code, null);
    }

    public static <T> Output<T> output(T data)
    {
        return new Output<>(Output.Code.OK, data);
    }

    public static Output outputOk()
    {
        return output(Output.Code.OK);
    }

    public static Output outputNotRegister()
    {
        return output(Output.Code.NotRegister);
    }

    public static Output outputError()
    {
        return output(Output.Code.Error);
    }

    public static Output outputNotLogin()
    {
        return output(Output.Code.NotLogin);
    }

    public static Output outputMaxSessions()
    {
        return output(Output.Code.MaxSessions);
    }

    public static Output outputParameterError()
    {
        return output(Output.Code.ParameterError);
    }

    public static Output outputUserExists()
    {
        return output(Output.Code.UserExists);
    }

    public static Output outputNotBelong()
    {
        return output(Output.Code.NotBelong);
    }

    public static Output outputMissionStatusError()
    {
        return output(Output.Code.MissionStatusError);
    }

    public static Output outputMaxCount()
    {
        return output(Output.Code.MaxCount);
    }

    public static Output outputInsufficientBalance()
    {
        return output(Output.Code.InsufficientBalance);
    }

    public static Output outputNotAuth()
    {
        return output(Output.Code.NotAuth);
    }
}
