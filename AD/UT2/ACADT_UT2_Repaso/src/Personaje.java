import java.util.ArrayList;
import java.util.List;

public class Personaje {
    private List<String> hobbits = new ArrayList<>();
    private List<String> hombres = new ArrayList<>();
    private List<String> elfos = new ArrayList<>();
    private List<String> enanos = new ArrayList<>();
    private List<String> magos = new ArrayList<>();
    private List<String> villanos = new ArrayList<>();

    public List<String> getHobbits() { return hobbits; }
    public void setHobbits(List<String> hobbits) { this.hobbits = hobbits; }

    public List<String> getHombres() { return hombres; }
    public void setHombres(List<String> hombres) { this.hombres = hombres; }

    public List<String> getElfos() { return elfos; }
    public void setElfos(List<String> elfos) { this.elfos = elfos; }

    public List<String> getEnanos() { return enanos; }
    public void setEnanos(List<String> enanos) { this.enanos = enanos; }

    public List<String> getMagos() { return magos; }
    public void setMagos(List<String> magos) { this.magos = magos; }

    public List<String> getVillanos() { return villanos; }
    public void setVillanos(List<String> villanos) { this.villanos = villanos; }
}