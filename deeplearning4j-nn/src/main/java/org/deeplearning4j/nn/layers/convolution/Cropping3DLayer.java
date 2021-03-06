package org.deeplearning4j.nn.layers.convolution;

import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.gradient.DefaultGradient;
import org.deeplearning4j.nn.gradient.Gradient;
import org.deeplearning4j.nn.layers.AbstractLayer;
import org.deeplearning4j.nn.workspace.ArrayType;
import org.deeplearning4j.nn.workspace.LayerWorkspaceMgr;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;

import static org.nd4j.linalg.indexing.NDArrayIndex.all;
import static org.nd4j.linalg.indexing.NDArrayIndex.interval;

/**
 * Cropping layer for 3D convolutional neural networks.
 * Allows cropping to be done separately for upper and lower bounds of
 * depth, height and width dimensions.
 *
 * @author Max Pumperla
 */
public class Cropping3DLayer extends AbstractLayer<org.deeplearning4j.nn.conf.layers.convolutional.Cropping3D> {

    private int[] cropping; //[cropLeftD, cropRightD, cropLeftH, cropRightH, cropLeftW, cropRightW]

    public Cropping3DLayer(NeuralNetConfiguration conf) {
        super(conf);
        this.cropping = ((org.deeplearning4j.nn.conf.layers.convolutional.Cropping3D) conf.getLayer()).getCropping();
    }

    @Override
    public boolean isPretrainLayer() {
        return false;
    }

    @Override
    public void clearNoiseWeightParams() {
        //No op
    }

    @Override
    public Type type() {
        return Type.CONVOLUTIONAL3D;
    }

    @Override
    public Pair<Gradient, INDArray> backpropGradient(INDArray epsilon, LayerWorkspaceMgr workspaceMgr) {
        int[] inShape = input.shape();
        INDArray epsNext = workspaceMgr.create(ArrayType.ACTIVATION_GRAD, inShape, 'c');
        INDArray epsNextSubset = inputSubset(epsNext);
        epsNextSubset.assign(epsilon);
        return new Pair<>((Gradient) new DefaultGradient(), epsNext);
    }


    @Override
    public INDArray activate(boolean training, LayerWorkspaceMgr workspaceMgr) {
        assertInputSet(false);
        INDArray ret = inputSubset(input);
        ret = workspaceMgr.leverageTo(ArrayType.ACTIVATIONS, ret);
        workspaceMgr.validateArrayLocation(ArrayType.ACTIVATIONS, ret, false, false);
        return ret;
    }

    @Override
    public Layer clone() {
        return new Cropping3DLayer(conf.clone());
    }

    @Override
    public double calcL1(boolean backpropParamsOnly) {
        return 0;
    }

    @Override
    public double calcL2(boolean backpropParamsOnly) {
        return 0;
    }

    private INDArray inputSubset(INDArray from){
        //NCDHW format
        return from.get(all(), all(),
                interval(cropping[0], from.size(2)-cropping[1]),
                interval(cropping[2], from.size(3)-cropping[3]),
                interval(cropping[4], from.size(4)-cropping[5]));
    }
}
