package mutexes;

public class Mutex {

    public int semWait(Resource resource) {
        Mutexable check = getSubclassInstance(resource);
        return check.semWait();
    }


    public int semSignal(Resource resource) {
        Mutexable check = getSubclassInstance(resource);
        return check.semWait();
    }
    private Mutexable getSubclassInstance(Resource resource)
    {
        if(resource.equals(Resource.file))
            return new AccessingFileMutex();
        else if (resource.equals(Resource.userInput))
            return new TakingInputMutex();
        else
            return new OutputtingScreen();

    };
}
