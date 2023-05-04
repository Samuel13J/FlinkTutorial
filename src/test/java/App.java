import org.junit.Test;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 * @author wangjie
 * @create 2023-05-04 10:40
 */
public class App {
    @Test
    public void getPythonParam() throws Exception {
        System.out.println("hello world");
        PythonInterpreter pythonInterpreter = new PythonInterpreter();
        pythonInterpreter.exec("a=[2,3,1,9,0,5]");
        pythonInterpreter.exec("print(sorted(a))");

        pythonInterpreter.execfile("src/test/java/Javamix.py");
//        如果有main方法会执行main方法
//        第一个参数为期望获得的参数（变量）的名字，第二个参数为期望返回的对象类型
        PyFunction function = pythonInterpreter.get("add", PyFunction.class);

        int a=1, b=2;
        System.out.println("调用 Python");
//        调用函数，如果函数需要参数，在java中必须先将参数转化为对应的 "python类型"
        PyObject pyObject = function.__call__(new PyInteger(a), new PyInteger(2));
        System.out.println("add的答案是："+pyObject);
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
}
