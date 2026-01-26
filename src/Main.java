//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    RungeKuttaBase rungeKuttaBase = new RungeKuttaBase();
    double posInicial=1;
    rungeKuttaBase.RK4(1,1.5,20,1);
    ArrayList<String> milista = new ArrayList<>();
    //rungeKutta.RK4(0,0.2,2,1);
}
