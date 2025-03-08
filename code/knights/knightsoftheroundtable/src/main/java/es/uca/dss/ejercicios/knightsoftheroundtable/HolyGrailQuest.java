package es.uca.dss.ejercicios.knightsoftheroundtable;

public class HolyGrailQuest implements Quest{
    public HolyGrailQuest() {
    }

    public HolyGrail embark()
            throws QuestFailedException {
        HolyGrail grail = null;
        // Look for grail
        try {
            Thread.sleep(2000);
            grail = new HolyGrail();
        } catch (Exception e) {
            throw new QuestFailedException();
        }
        return grail;
    }
}


