package it.unisalento.recproject.recprojectio.di;

public class SolarPanel implements IRenewableSource{
    private final double efficiency;
    private final double area;

    public SolarPanel(double efficiency, double area) {
        this.efficiency = efficiency;
        this.area = area;
    }

    @Override
    public void initialize() {
        System.out.println("Initializing solar panel with efficiency: " + efficiency + " and area: " + area);
    }

    @Override
    public double generateEnergy() {
        return efficiency * area * 100; // Simplified calculation
    }
}
