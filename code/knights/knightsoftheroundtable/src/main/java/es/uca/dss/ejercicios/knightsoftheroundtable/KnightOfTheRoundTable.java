package es.uca.dss.ejercicios.knightsoftheroundtable;

import org.springframework.beans.factory.annotation.Autowired;

public class KnightOfTheRoundTable implements Knight {
    private Quest quest;

    @Autowired
    public KnightOfTheRoundTable(Quest quest) {
        this.quest = quest;
    }

    public QuestItem embarkOnQuest()
            throws QuestFailedException {
        return quest.embark();
    }
}