package SwitchAnalyzer.Network.ErrorDetection;

import SwitchAnalyzer.Network.Header;

public abstract class ErrorDetectingAlgorithms extends Header {
   String AlgorithmName;

    public ErrorDetectingAlgorithms()
    {

    }

    public abstract boolean isAlgorithmCorrect(byte [] packet);
}
