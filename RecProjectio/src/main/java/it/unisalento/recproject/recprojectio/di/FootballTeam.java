package it.unisalento.recproject.recprojectio.di;

import org.springframework.stereotype.Component;

@Component
public class FootballTeam implements IRenewableSource{
    @Override
    public void initialize()
    {
        System.out.println("Initializing Football Team");
    }

    @Override
    public double generateEnergy() {
        return 0;
    }
}
