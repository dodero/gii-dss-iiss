package es.uca.dss.ejercicios.knightsoftheroundtable;

public interface Knight{
    QuestItem embarkOnQuest() throws QuestFailedException;
}

class QuestFailedException extends RuntimeException {

}