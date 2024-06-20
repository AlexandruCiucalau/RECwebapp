package it.unisalento.recproject.recprojectio.di;

public class WindTurbine implements IRenewableSource{
    private final double bladeLength;

    public WindTurbine(double bladeLength) {
        this.bladeLength = bladeLength;
    }

    @Override
    public void initialize() {
        System.out.println("Initializing wind turbine with blade length: " + bladeLength);
    }

    @Override
    public double generateEnergy() {
        return Math.pow(bladeLength, 2) * 50; // Simplified calculation
    }
}
