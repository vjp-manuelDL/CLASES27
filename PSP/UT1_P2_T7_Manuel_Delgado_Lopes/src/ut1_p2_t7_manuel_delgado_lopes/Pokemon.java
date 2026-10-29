package ut1_p2_t7_manuel_delgado_lopes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private String nombre;
    private int id;
    private int altura;
    private int peso;
    private String tipos;
    private String debilidades;
    private int hp;
    private int ataque;
    private int defensa;
    private int ataqueSpecial;
    private int defensaSpecial;
    private int velocidad;
    private String urlImagenNormal;
    private String urlImagenShiny;

    public Pokemon(String nombre, int id, int altura, int peso, String tipos, String debilidades,
                   int hp, int ataque, int defensa, int ataqueSpecial, int defensaSpecial, int velocidad, 
                   String urlImagenNormal, String urlImagenShiny) {
        this.nombre = nombre;
        this.id = id;
        this.altura = altura;
        this.peso = peso;
        this.tipos = tipos;
        this.debilidades = debilidades;
        this.hp = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.ataqueSpecial = ataqueSpecial;
        this.defensaSpecial = defensaSpecial;
        this.velocidad = velocidad;
        this.urlImagenNormal = urlImagenNormal;
        this.urlImagenShiny = urlImagenShiny;
    }

    public static Pokemon consultarPokemon(String busqueda) {
        try {
            String urlString = "https://pokeapi.co/api/v2/pokemon/" + busqueda.toLowerCase().trim();
            URL url = new URL(urlString);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");

            if (conexion.getResponseCode() == 200) {
                BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String linea;
                while ((linea = lector.readLine()) != null) {
                    jsonBuilder.append(linea);
                }
                lector.close();

                String json = jsonBuilder.toString();

                String nombre = extraerValorJson(json, "name");
                int id = Integer.parseInt(extraerValorJson(json, "id"));
                int altura = Integer.parseInt(extraerValorJson(json, "height"));
                int peso = Integer.parseInt(extraerValorJson(json, "weight"));

                int hp = extraerStat(json, "hp");
                int ataque = extraerStat(json, "attack");
                int defensa = extraerStat(json, "defense");
                int atEsp = extraerStat(json, "special-attack");
                int defEsp = extraerStat(json, "special-defense");
                int velocidad = extraerStat(json, "speed");

                String imgNormal = extraerUrlSprite(json, "front_default");
                String imgShiny = extraerUrlSprite(json, "front_shiny");

                String tiposStr = extraerTipos(json);
                String debilidadesStr = calcularDebilidades(json);

                return new Pokemon(nombre, id, altura, peso, tiposStr, debilidadesStr, 
                                   hp, ataque, defensa, atEsp, defEsp, velocidad, imgNormal, imgShiny);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String extraerValorJson(String json, String campo) {
        try {
            String buscar = "\"" + campo + "\":";
            int inicio = json.indexOf(buscar);
            if (inicio == -1) return "0";
            inicio += buscar.length();
            int fin = json.indexOf(",", inicio);
            if (fin == -1) fin = json.indexOf("}", inicio);
            return json.substring(inicio, fin).replace("\"", "").trim();
        } catch (Exception e) {
            return "0";
        }
    }

    private static int extraerStat(String json, String nombreStat) {
        try {
            int idxStat = json.indexOf("\"name\":\"" + nombreStat + "\"");
            if (idxStat == -1) return 0;
            String sub = json.substring(idxStat);
            return Integer.parseInt(extraerValorJson(sub, "base_stat"));
        } catch (Exception e) {
            return 0;
        }
    }

    private static String extraerUrlSprite(String json, String tipoSprite) {
        try {
            int idxSprites = json.indexOf("\"sprites\":");
            if (idxSprites == -1) return "";
            String sub = json.substring(idxSprites);
            return extraerValorJson(sub, tipoSprite);
        } catch (Exception e) {
            return "";
        }
    }

    private static String extraerTipos(String json) {
        try {
            List<String> listaTipos = new ArrayList<>();
            int index = 0;
            while ((index = json.indexOf("\"type\":{", index)) != -1) {
                int nameIdx = json.indexOf("\"name\":\"", index);
                if (nameIdx != -1) {
                    int start = nameIdx + 8;
                    int end = json.indexOf("\"", start);
                    listaTipos.add(json.substring(start, end));
                }
                index++;
            }
            return String.join(", ", listaTipos);
        } catch (Exception e) {
            return "Desconocido";
        }
    }

    private static String calcularDebilidades(String json) {
        try {
            List<String> debilidades = new ArrayList<>();
            int index = 0;
            while ((index = json.indexOf("\"type\":{", index)) != -1) {
                int nameIdx = json.indexOf("\"name\":\"", index);
                if (nameIdx != -1) {
                    int start = nameIdx + 8;
                    int end = json.indexOf("\"", start);
                    String tipo = json.substring(start, end);
                    
                    URL url = new URL("https://pokeapi.co/api/v2/type/" + tipo);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String l;
                        while ((l = br.readLine()) != null) sb.append(l);
                        br.close();
                        
                        String tipoJson = sb.toString();
                        int dmgFrom = tipoJson.indexOf("\"double_damage_from\":");
                        if (dmgFrom != -1) {
                            int arrStart = tipoJson.indexOf("[", dmgFrom);
                            int arrEnd = tipoJson.indexOf("]", dmgFrom);
                            if (arrStart != -1 && arrEnd != -1 && arrEnd > arrStart) {
                                String subArr = tipoJson.substring(arrStart, arrEnd);
                                int tIdx = 0;
                                while ((tIdx = subArr.indexOf("\"name\":\"", tIdx)) != -1) {
                                    int tStart = tIdx + 8;
                                    int tEnd = subArr.indexOf("\"", tStart);
                                    String deb = subArr.substring(tStart, tEnd);
                                    if (!debilidades.contains(deb)) {
                                        debilidades.add(deb);
                                    }
                                    tIdx++;
                                }
                            }
                        }
                    }
                }
                index++;
            }
            return debilidades.isEmpty() ? "Ninguna" : String.join(", ", debilidades);
        } catch (Exception e) {
            return "No disponible";
        }
    }

    public String getNombre() { return nombre; }
    public int getId() { return id; }
    public int getAltura() { return altura; }
    public int getPeso() { return peso; }
    public String getTipos() { return tipos; }
    public String getDebilidades() { return debilidades; }
    public int getHp() { return hp; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getAtaqueSpecial() { return ataqueSpecial; }
    public int getDefensaSpecial() { return defensaSpecial; }
    public int getVelocidad() { return velocidad; }
    public String getUrlImagenNormal() { return urlImagenNormal; }
    public String getUrlImagenShiny() { return urlImagenShiny; }
}