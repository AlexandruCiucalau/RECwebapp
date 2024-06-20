package it.unisalento.recproject.recprojectio.di;

import org.springframework.stereotype.Component;

@Component
public class BasketTeam implements IRenewableSource{
    @Override
    public void initialize() {
        System.out.println("Basket Team initializiato");
    }

    @Override
    public double generateEnergy() {
        return 0;
    }
}
