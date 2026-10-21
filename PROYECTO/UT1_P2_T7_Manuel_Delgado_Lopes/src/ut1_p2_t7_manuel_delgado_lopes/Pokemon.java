package ut1_p2_t7_manuel_delgado_lopes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pokemon {
    private String nombre;
    private int id, altura, peso, hp, ataque, defensa, ataqueSpecial, defensaSpecial, velocidad;
    private String tipos, debilidades, habilidades, movimientos, urlImagenNormal, urlImagenShiny;

    public Pokemon(String nombre, int id, int altura, int peso, String tipos, String debilidades,
                   String habilidades, String movimientos,
                   int hp, int ataque, int defensa, int ataqueSpecial, int defensaSpecial, int velocidad, 
                   String urlImagenNormal, String urlImagenShiny) {
        this.nombre = nombre;
        this.id = id;
        this.altura = altura;
        this.peso = peso;
        this.tipos = tipos;
        this.debilidades = debilidades;
        this.habilidades = habilidades;
        this.movimientos = movimientos;
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
            // Limpiamos el texto para que la PokeAPI lo acepte (ej: Mime Jr. -> mime-jr)
            String nombreLimpio = busqueda.toLowerCase().trim()
                .replace("♀", "-f").replace("♂", "-m")
                .replace("'", "").replace(".", "").replace(" ", "-");

            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + nombreLimpio);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            
            // --- SOLUCIÓN AL CUELGUE ---
            // Engañamos a la API para que no bloquee a Java y ponemos un límite de 5 segundos
            conexion.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(5000);

            if (conexion.getResponseCode() == 200) {
                BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String linea;
                while ((linea = lector.readLine()) != null) jsonBuilder.append(linea);
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

                String tiposStr = extraerListaSimple(json, "\"type\":{", "\"name\":\"");
                String habStr = extraerListaSimple(json, "\"ability\":{", "\"name\":\"");
                String movsStr = extraerPrimerosMovimientos(json, 5);
                String debilidadesStr = calcularDebilidadesReales(json);

                return new Pokemon(nombre, id, altura, peso, tiposStr, debilidadesStr, habStr, movsStr,
                                   hp, ataque, defensa, atEsp, defEsp, velocidad, imgNormal, imgShiny);
            }
        } catch (Exception e) {
            System.out.println("Error o timeout en la conexión: " + e.getMessage());
        }
        return null; // Si hay error, devuelve null y la interfaz mostrará el mensaje
    }

    private static String calcularDebilidadesReales(String jsonPokemon) {
        Map<String, Double> multiplicadores = new HashMap<>();
        try {
            int index = 0;
            while ((index = jsonPokemon.indexOf("\"type\":{", index)) != -1) {
                int nameIdx = jsonPokemon.indexOf("\"name\":\"", index);
                if (nameIdx != -1) {
                    int start = nameIdx + 8;
                    int end = jsonPokemon.indexOf("\"", start);
                    String tipo = jsonPokemon.substring(start, end);
                    
                    URL url = new URL("https://pokeapi.co/api/v2/type/" + tipo);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);
                    
                    if (con.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String l;
                        while ((l = br.readLine()) != null) sb.append(l);
                        br.close();
                        
                        String tipoJson = sb.toString();
                        procesarMultiplicadores(tipoJson, "double_damage_from", 2.0, multiplicadores);
                        procesarMultiplicadores(tipoJson, "half_damage_from", 0.5, multiplicadores);
                        procesarMultiplicadores(tipoJson, "no_damage_from", 0.0, multiplicadores);
                    }
                }
                index += 8;
            }
            
            List<String> debilidades = new ArrayList<>();
            for (Map.Entry<String, Double> entry : multiplicadores.entrySet()) {
                if (entry.getValue() > 1.0) debilidades.add(entry.getKey() + " (x" + entry.getValue() + ")");
            }
            return debilidades.isEmpty() ? "Ninguna" : String.join(", ", debilidades);
            
        } catch (Exception e) {
            return "Cálculo no disponible";
        }
    }

    private static void procesarMultiplicadores(String json, String categoria, double factor, Map<String, Double> map) {
        int idx = json.indexOf("\"" + categoria + "\":");
        if (idx == -1) return;
        int arrStart = json.indexOf("[", idx);
        int arrEnd = json.indexOf("]", idx);
        if (arrStart != -1 && arrEnd != -1) {
            String sub = json.substring(arrStart, arrEnd);
            int tIdx = 0;
            while ((tIdx = sub.indexOf("\"name\":\"", tIdx)) != -1) {
                int tStart = tIdx + 8;
                int tEnd = sub.indexOf("\"", tStart);
                String tipo = sub.substring(tStart, tEnd);
                map.put(tipo, map.getOrDefault(tipo, 1.0) * factor);
                tIdx = tEnd;
            }
        }
    }

    private static String extraerListaSimple(String json, String busquedaPadre, String busquedaNombre) {
        List<String> lista = new ArrayList<>();
        int index = 0;
        while ((index = json.indexOf(busquedaPadre, index)) != -1) {
            int nameIdx = json.indexOf(busquedaNombre, index);
            if (nameIdx != -1) {
                int start = nameIdx + busquedaNombre.length();
                int end = json.indexOf("\"", start);
                lista.add(json.substring(start, end));
            }
            index += busquedaPadre.length();
        }
        return String.join(", ", lista);
    }

    private static String extraerPrimerosMovimientos(String json, int limite) {
        List<String> lista = new ArrayList<>();
        int index = 0, count = 0;
        while ((index = json.indexOf("\"move\":{", index)) != -1 && count < limite) {
            int nameIdx = json.indexOf("\"name\":\"", index);
            if (nameIdx != -1) {
                int start = nameIdx + 8;
                int end = json.indexOf("\"", start);
                lista.add(json.substring(start, end));
                count++;
            }
            index += 8;
        }
        return String.join(", ", lista) + (count == limite ? "..." : "");
    }

    private static String extraerValorJson(String json, String campo) {
        try {
            int inicio = json.indexOf("\"" + campo + "\":");
            if (inicio == -1) return "0";
            inicio += campo.length() + 3;
            int fin = json.indexOf(",", inicio);
            if (fin == -1) fin = json.indexOf("}", inicio);
            return json.substring(inicio, fin).replace("\"", "").trim();
        } catch (Exception e) { return "0"; }
    }

    private static int extraerStat(String json, String nombreStat) {
        try {
            int idxStat = json.indexOf("\"name\":\"" + nombreStat + "\"");
            if (idxStat == -1) return 0;
            String sub = json.substring(idxStat);
            return Integer.parseInt(extraerValorJson(sub, "base_stat"));
        } catch (Exception e) { return 0; }
    }

    private static String extraerUrlSprite(String json, String tipoSprite) {
        try {
            int idxSprites = json.indexOf("\"sprites\":");
            if (idxSprites == -1) return "";
            return extraerValorJson(json.substring(idxSprites), tipoSprite);
        } catch (Exception e) { return ""; }
    }

    public String getNombre() { return nombre; }
    public int getId() { return id; }
    public int getAltura() { return altura; }
    public int getPeso() { return peso; }
    public String getTipos() { return tipos; }
    public String getDebilidades() { return debilidades; }
    public String getHabilidades() { return habilidades; }
    public String getMovimientos() { return movimientos; }
    public int getHp() { return hp; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getAtaqueSpecial() { return ataqueSpecial; }
    public int getDefensaSpecial() { return defensaSpecial; }
    public int getVelocidad() { return velocidad; }
    public String getUrlImagenNormal() { return urlImagenNormal; }
    public String getUrlImagenShiny() { return urlImagenShiny; }
}