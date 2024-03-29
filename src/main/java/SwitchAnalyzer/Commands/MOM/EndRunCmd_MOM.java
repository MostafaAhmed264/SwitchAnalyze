package SwitchAnalyzer.Commands.MOM;

import SwitchAnalyzer.Collectors.MOMConsumer;
import SwitchAnalyzer.Commands.ICommandMOM;
import SwitchAnalyzer.Commands.Master.EndCmd_Master;
import SwitchAnalyzer.Database.DBInsert;
import SwitchAnalyzer.EndCmdUI;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EndRunCmd_MOM implements ICommandMOM
{
    public void processCmd()
    {
          System.out.println("END");
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        MOMConsumer.results.put("EndTime", dateFormat.format(cal.getTime()));

        GlobalVariable.retrieveDataFromNode = false;
        GlobalVariable.endRun = true;

        genCmd(0);
    }

    public void genCmd(int switchPortId)
    {
        String json = JSONConverter.toJSON(new EndCmd_Master(switchPortId));
        json = "6" + json;
        System.out.println(json + "PRODUCEDDDD FROM END");
        MainHandler_MOM.cmdProducer.produce(json, Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }
}
