package io.puharesource.mc.commafy.protocol.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import io.puharesource.mc.commafy.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ProtocolChatListener extends PacketAdapter {
    public ProtocolChatListener(Main plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.CHAT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrappedChatComponent chat = event.getPacket().getChatComponents().read(0);
        try {
            chat.setJson(formatJson(chat.getJson()));
            event.getPacket().getChatComponents().write(0, chat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String formatNumber(BigDecimal number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        return new DecimalFormat(plugin.getConfig().getString("format"), symbols).format(number);
    }

    String formatJson(String jsonString) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        JSONArray jsonArray = (JSONArray) json.get("extra");

        for(int i = 0; jsonArray.size() > i; i++) {
            if(!(jsonArray.get(i) instanceof JSONObject)) continue;
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            String text = (String) jsonObject.get("text");
            for(String word : text.split(" ")) {
                try {
                    if(plugin.getConfig().getBoolean("forcedMode"))
                        word = word.replaceAll("[^0-9.]", "");
                    else if(plugin.getConfig().getBoolean("currencyMode")) {
                        if(word.startsWith("$") || word.startsWith("£") || word.startsWith("€"))
                            word = word.substring(1);
                        else if (word.endsWith("$") || word.endsWith("£") || word.endsWith("€"))
                            word = word.substring(0, word.length() - 1);
                    }

                    BigDecimal number = BigDecimal.valueOf(Double.valueOf(word));
                    text = text.replaceFirst(word, formatNumber(number));
                } catch (NumberFormatException e) {}
            }
            jsonObject.put("text", text);
            jsonArray.set(i, jsonObject);
        }
        json.put("extra", jsonArray);
        return json.toJSONString();
    }
}
