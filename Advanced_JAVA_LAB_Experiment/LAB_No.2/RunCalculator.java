public class RunCalculator {
    public static void main(String[] args) {
        ViewsCalculator vc = new ViewsCalculator();
        ModelsCalculator mc = new ModelsCalculator();
        new ControllerCalculator(vc, mc);
    }
}