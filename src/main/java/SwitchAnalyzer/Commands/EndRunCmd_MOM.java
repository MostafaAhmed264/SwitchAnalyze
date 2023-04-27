package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Collectors.MOMConsumer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EndRunCmd_MOM implements ICommandMOM
{
    public void processCmd()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        MOMConsumer.results.put("EndTime", dateFormat.format(cal.getTime()));
        String json = JSONConverter.toJSON(MOMConsumer.results);
        try { JettyWebSocketServer.writeMessage(json); }
        catch (Exception e) { throw new RuntimeException(e); }
        //Add Insert Here for DB RUN
        GlobalVariable.retrieveDataFromNode = false;
        GlobalVariable.endRun = true;
        genCmd(0);
    }

    public void genCmd(int switchPortId)
    {
        String json = JSONConverter.toJSON(new EndCmd_Master(switchPortId));
        json = GlobalVariable.CMD_IDX.ENDRUN_IDX + json;
        MainHandler_MOM.cmdProducer.produce(json, Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }
}
