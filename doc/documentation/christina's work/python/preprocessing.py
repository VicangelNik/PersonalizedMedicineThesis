#!/usr/bin/python
import pickle
import threading
from queue import Queue
from time import clock
import graphviz

import numpy as np
import matplotlib.pyplot as plt
import sklearn
from networkx import write_multiline_adjlist, read_multiline_adjlist
from networkx.drawing.nx_pydot import graphviz_layout
from networkx.drawing.nx_pydot import pydot_layout
from numpy import random
from numpy.matlib import rand
from sklearn.cluster import KMeans
from pandas.core.arrays import categorical
from sklearn.cross_validation import cross_val_score
from sklearn.ensemble import RandomForestClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import Imputer, QuantileTransformer
from sklearn import decomposition, tree
from mpl_toolkits.mplot3d import Axes3D
from sklearn.preprocessing import MinMaxScaler
from sklearn import decomposition
import networkx as nx
import itertools
import sys
from scipy.stats import pearsonr
from sklearn.tree import DecisionTreeClassifier


def locateTargetField():
    # Open file
    fInput = open("control.csv", "r")
    # fInput = open("allTogether.csv", "r")

    # Read first line
    sFirstLine = fInput.readline()
    # Search for index of field of interest (e.g. ...death...)
    iCnt = 0
    for sCur in sFirstLine.split():
        # if ("m_" not in sCur):
        #     print sCur
        if "Death" in sCur:
            print("Found field '%s' with index %d" % (sCur, iCnt))
        iCnt += 1

    fInput.close()


def determineCompleteSamples():
    ### Determine instances that are mapped to all modalities
    # All samples dict
    dSamples = dict()

    # Open sample sheet file
    fModalities = open('sampleSheet.tsv')
    # Ignore header
    sLine = fModalities.readline()
    # For every line
    while sLine != "":
        sLine = fModalities.readline()
        if sLine == "":
            break

        # Get sample ID (field #7 out of 8)
        sSampleID = sLine.split("\t")[6].strip()
        # Get data type (4) and remember it
        sDataType = sLine.split("\t")[3].strip()
        # Initialize samples that were not encountered earlier
        if sSampleID not in dSamples.keys():
            dSamples[sSampleID] = dict()
        # Add data type to list
        dSamples[sSampleID][sDataType] = 1
    fModalities.close()

    # For all collected sample IDs
    print("+ Complete instances:")
    for sSampleID, lDataTypes in dSamples.items():
        # Output which one has exactly three data types
        if len(lDataTypes) == 3:
            print(sSampleID)

    print("\n\n- Incomplete instances:")
    for sSampleID, lDataTypes in dSamples.items():
        # Output which one has exactly three data types
        if len(lDataTypes) < 3:
            print(sSampleID)


def PCAOnControl():

    print("Opening file...")
    mFeatures_noNaNs, vClass = initializeFeatureMatrices(False, True)
    mFeatures_noNaNs = getControlFeatureMatrix(mFeatures_noNaNs, vClass)
    print("Opening file... Done.")
    X, pca3DRes = getPCA(mFeatures_noNaNs, 3)

    fig = draw3DPCA(X, pca3DRes)

    fig.savefig("controlPCA3D.pdf", bbox_inches='tight')

def PCAOnTumor():

    print("Opening file...")
    mFeatures_noNaNs, vClass = initializeFeatureMatrices(False, True)
    mFeatures_noNaNs = getNonControlFeatureMatrix(mFeatures_noNaNs, vClass)
    print("Opening file... Done.")
    X, pca3DRes = getPCA(mFeatures_noNaNs, 3)

    fig = draw3DPCA(X, pca3DRes)

    fig.savefig("tumorPCA3D.pdf", bbox_inches='tight')


def draw3DPCA(X, pca3DRes, c=None, cmap=plt.cm.gnuplot, spread=False):
    # Percentage of variance explained for each components
    print('explained variance ratio (first 3 components): %s'
          % str(pca3DRes.explained_variance_ratio_))

    if spread:
        X = QuantileTransformer(output_distribution='uniform').fit_transform(X)

    fig = plt.figure()
    plt.clf()
    ax = fig.add_subplot(111, projection='3d')
    ax.scatter(X[:, 0], X[:, 1], X[:, 2], edgecolor='k', c=c, cmap=cmap, depthshade=False)
    ax.set_xlabel("X coordinate (%4.2f)" % (pca3DRes.explained_variance_ratio_[0]))
    ax.set_ylabel("Y coordinate (%4.2f)" % (pca3DRes.explained_variance_ratio_[1]))
    ax.set_zlabel("Z coordinate (%4.2f)" % (pca3DRes.explained_variance_ratio_[2]))
    ax.set_xticklabels([])
    ax.set_yticklabels([])
    ax.set_zticklabels([])

    fig.show()
    return fig


def getPCA(mFeatures_noNaNs, n_components=3):
    pca = decomposition.PCA(n_components)
    pca.fit(mFeatures_noNaNs)
    X = pca.transform(mFeatures_noNaNs)
    return X, pca


def rand_jitter(arr):
    stdev = .01 * (max(arr) - min(arr))
    return arr + np.random.randn(len(arr)) * stdev


# def expander(t):
#     return log10(t)

def convertTumorType(s):
    fRes = float(["not reported", "stage i", "stage ii", "stage iii", "stage iv", "stage v"].index(s.decode('UTF-8')))
    if int(fRes) == 0:
        return np.nan
    return fRes


def PCAOnAllData():
    # Check if we need to reset the files
    bResetFiles = False
    if len(sys.argv) > 1:
        if "-resetFiles" in sys.argv:
            bResetFiles = True


    # Initialize feature matrices
    mFeatures_noNaNs, vClass = initializeFeatureMatrices(bResetFiles=bResetFiles, bPostProcessing=True)

    print("Applying PCA...")
    X, pca3D = getPCA(mFeatures_noNaNs, 3)

    # Spread
    print("Applying PCA... Done.")

    # Percentage of variance explained for each components
    print('explained variance ratio (first 3 components): %s'
          % str(pca3D.explained_variance_ratio_))

    print('3 components values: %s'
          % str(X))

    print("Plotting PCA graph...")
    # Assign colors
    aCategories, y = np.unique(vClass, return_inverse=True)
    draw3DPCA(X,pca3D,c=y/2)
    # DEBUG LINES
    print("Returning categories: \n %s" % (str(aCategories)))
    print("Returning categorical vector: \n %s" % (str(y)))
    ############
    # fig = plt.figure()
    # plt.clf()
    # ax = fig.add_subplot(111, projection='3d')
    # ax.scatter(X[:, 0], X[:, 1], X[:, 2],
    #            c=y / 2, cmap=plt.cm.gnuplot, depthshade=False, marker='.')
    #
    # ax.set_xlabel("X coordinate (%4.2f)" % (pca3D.explained_variance_ratio_[0]))
    # ax.set_ylabel("Y coordinate (%4.2f)" % (pca3D.explained_variance_ratio_[1]))
    # # ax.set_zlabel("Z coordinate (%4.2f)"%(pca.explained_variance_ratio_[2]))
    #
    # ax.set_xticklabels([])
    # ax.set_yticklabels([])
    # # ax.set_zticklabels([])
    # plt.show()

    print("Plotting PCA graph... Done.")


def initializeFeatureMatrices(bResetFiles = False, bPostProcessing=True, bNormalize=True):
    # Read control
    print("Opening files...")
    # import pandas as pd
    # df = pd.read_csv('./patientAndControlData.csv', sep='\t')
    # df.reindex_axis(sorted(df.columns), axis=1)
    # datafile = df.as_matrix()

    try:
        if bResetFiles:
            raise Exception("User requested file reset...")
        print("Trying to load saved data...")
        datafile = np.load("patientAndControlData.mat.npy")
        labelfile = np.load("patientAndControlDataLabels.mat.npy")
        clinicalfile = loadTumorStage()
        print("Trying to load saved data... Done.")
    except Exception as eCur:
        print(str(eCur))
        print("Trying to load saved data... Failed.")
        print("Trying to load saved data from CSV...")
        fControl = open("./patientAndControlData.csv", "r")
        print("Loading labels and ids...")
        labelfile = np.genfromtxt(fControl, skip_header=1, usecols=(0,73664),
                                  missing_values=['NA', "na", '-', '--', 'n/a'], delimiter="\t",
                                  dtype=np.dtype("object")
                                  )
        fControl.close()
        print("Loading labels and ids... Done.")
        # # DEBUG LINES
        # print(str(labelfile))
        # #############

        clinicalfile = loadTumorStage()

        datafile = loadPatientAndControlData()
        print("Trying to load saved data from CSV... Done.")

        # Saving
        saveLoadedData(datafile, labelfile)

    print("Opening files... Done.")
    # Split feature set to features/target field
    mFeatures, vClass = splitFeatures(clinicalfile, datafile, labelfile)

    mControlFeatureMatrix = getControlFeatureMatrix(mFeatures, vClass)

    if bPostProcessing:
        mFeatures = postProcessFeatures(mFeatures, mControlFeatureMatrix)

    if bNormalize:
        mFeatures = normalizeDataByControl(mFeatures, mControlFeatureMatrix)
    return mFeatures, vClass


def postProcessFeatures(mFeatures, mControlFeatures):
    print("Replacing NaNs from feature set...")
    # DEBUG LINES
    print("Data shape before replacement: %s"%(str(np.shape(mFeatures))))
    #############

    # WARNING: Imputer also throws away columns it does not like
    # imputer = Imputer(strategy="mean", missing_values="NaN", verbose=1)
    # mFeatures_noNaNs = imputer.fit_transform(mFeatures)

    # Extract means per control col
    mMeans = np.nanmean(mControlFeatures, axis=0)
    # Find nans
    inds = np.where(np.isnan(mFeatures))
    # Do replacement
    mFeatures[inds] = np.take(mMeans, inds[1])

    # DEBUG LINES
    print("Data shape after replacement: %s"%(str(np.shape(mFeatures))))
    #############
    print("Replacing NaNs from feature set... Done.")
    return mFeatures


def splitFeatures(clinicalfile, datafile, labelfile):
    print("Splitting features...")
    print(np.size(datafile, 1))
    mFeaturesOnly = datafile[:, 1:73662]
    # Create matrix with extra column (to add tumor stage)
    iFeatCount = np.shape(mFeaturesOnly)[1] + 1
    mFeatures = np.zeros((np.shape(mFeaturesOnly)[0], iFeatCount))
    mFeatures[:, :-1] = mFeaturesOnly
    mFeatures[:, iFeatCount - 1] = np.nan
    # For every row
    for iCnt in range(np.shape(labelfile)[0]):
        condlist = clinicalfile[:, 0] == labelfile[iCnt, 0]
        # Create a converter
        tumorStageToInt = np.vectorize(convertTumorType)
        choicelist = tumorStageToInt(clinicalfile[:, 1])
        # Update the last feature, by joining on ID
        mFeatures[iCnt, iFeatCount - 1] = np.select(condlist, choicelist)
    vClass = labelfile[:, 1]
    # DEBUG LINES
    # print("Found classes:\n%s" % (str(vClass)))
    #############
    # DEBUG LINES
    # print("Found tumor types:\n%s" % (
    #     "\n".join(["%s:%s" % (x, y) for x, y in zip(labelfile[:, 0], mFeatures[:, iFeatCount - 1])])))
    #############
    print("Splitting features... Done.")
    return mFeatures, vClass


def saveLoadedData(datafile, labelfile):
    print("Saving data...")
    np.save("patientAndControlData.mat.npy", datafile)
    np.save("patientAndControlDataLabels.mat.npy", labelfile)
    print("Saving data... Done.")


def loadPatientAndControlData():
    print("Loading features...")
    fControl = open("./patientAndControlData.csv", "r")
    datafile = np.genfromtxt(fControl, skip_header=1, usecols=range(1, 73665 - 3),
                             missing_values=['NA', "na", '-', '--', 'n/a'], delimiter="\t",
                             dtype=np.dtype("float")
                             )
    fControl.close()
    print("Loading features... Done.")
    return datafile


def loadTumorStage():
    # Tumor stage
    print("Loading tumor stage...")
    fClinical = open("./clinicalAll.tsv", "r")
    # While loading stage, also convert string to integer
    clinicalfile = np.genfromtxt(fClinical, skip_header=1, usecols=(1, 11),
                                 missing_values=['NA', "na", '-', '--', 'n/a'], delimiter="\t",
                                 dtype=np.dtype("object"),
                                 # converters={11: lambda s : ["stage i", "stage ii", "stage iii", "stage iv", "stage v"].index(s)}
                                 )
    fClinical.close()
    print("Loading tumor stage... Done.")
    return clinicalfile


def ClusterAllData():
    # Initialize feature matrices
    mFeatures_noNaNs, vClass = initializeFeatureMatrices(bResetFiles=False, bPostProcessing=True)

    print("Separating instances per class...")
    # Perform clustering, initializing the clusters with a control and a patient
    # Set starting points
    npaControlFeatures = getControlFeatureMatrix(mFeatures_noNaNs, vClass)
    npaNonControlFeatures = getNonControlFeatureMatrix(mFeatures_noNaNs, vClass)

    npInitialCentroids = np.array( [np.nanmedian(npaControlFeatures[:,:], 0),
                           np.nanmedian(npaNonControlFeatures[:,:], 0 )])

    print("Separating instances per class... Done.")

    print("Applying k-means...")
    # Perform clustering
    clusterer = KMeans(2, npInitialCentroids, n_init=1)
    y_pred = clusterer.fit_predict(mFeatures_noNaNs)
    print("Applying k-means... Done.")

    print("Applying PCA for visualization...")
    X, pca3D = getPCA(mFeatures_noNaNs, 3)
    # X = QuantileTransformer(output_distribution='uniform').fit_transform(X)

    print("Applying PCA for visualization... Done.")

    draw3DPCA(X, pca3D, c=y_pred)
    aCategories, y = np.unique(vClass, return_inverse=True)
    draw3DPCA(X, pca3D, c=y)
    # splt = fig.add_subplot(122
    #                        # , projection='3d'
    #                       )
    # # Assign colors
    # aCategories, y = np.unique(vClass, return_inverse=True)
    # splt.scatter(X[:, 0], X[:, 1], c=y, cmap=plt.cm.gnuplot, marker='.'
    # # , depthshade=False
    #              )
    # splt.title = "True"

    print("Plotting... Done.")

    # Calculate performance (number/precent of misplaced controls, number/precent of misplaced tumor samples)


def getControlFeatureMatrix(mAllData, vLabels):
    choicelist = mAllData
    condlist = isEqualToString(vLabels, 'NormalTissue')
    return choicelist[condlist]

def isEqualToString(npaVector, sString):
    aRes = np.array([oCur.decode('UTF-8').strip() for oCur in npaVector[:]])
    aStr = np.array([ sString.strip() for oCur in npaVector[:] ])
    return aRes == aStr

def getNonControlFeatureMatrix(mAllData, vLabels):
    choicelist = mAllData
    condlist = isEqualToString(vLabels, 'PrimaryTumor')
    return choicelist[condlist]

def normalizeDataByControl(mFeaturesToNormalize, mControlData, logScale=True):
    print("Normalizing based on control set...")
    centroid = np.nanmean(mControlData[:,:], 0)
    # Using percentile change instead of ratio, to avoid lower bound problems
    mOut = ((mFeaturesToNormalize - centroid) + 10e-8) / (centroid + 10e-8)
    # DEBUG LINES
    print("Data shape before normalization: %s"%(str(np.shape(mFeaturesToNormalize))))
    #############
    if logScale:
        mOut = np.log2(2.0 + mOut) # Ascertain positive numbers
    # DEBUG LINES
    print("Data shape after normalization: %s"%(str(np.shape(mOut))))
    #############
    print("Normalizing based on control set... Done.")
    return mOut

def test():
    g = nx.Graph()
    g.add_path([1,2,3,4], weight=0.5)
    g.add_path([2, 6], weight=0.2)
    g.add_path([5,6], weight=0.8)

    for nNode in g.nodes():
        g.node[nNode]['weight'] = nNode * 10

    drawGraph(g)

    spreadingActivation(g)
    drawGraph(g)

def getFeatureNames():
    print("Loading feature names...")
    fControl = open("./patientAndControlData.csv", "r")
    saNames = fControl.readline().strip().split("\t")
    lFeatureNames = [ sName.strip() for sName in saNames ]
    fControl.close()
    print("Loading feature names... Done.")

    return lFeatureNames

def addEdgeAboveThreshold(i, qQueue):
    while True:
        # Get next feature index pair to handle
        iFirstFeatIdx, iSecondFeatIdx, g, mAllData, saFeatures, iFirstFeatIdx, iSecondFeatIdx, dEdgeThreshold = qQueue.get()

        # Fetch feature columns and calculate pearson
        vFirstRepr = mAllData[:, iFirstFeatIdx]
        vSecondRepr = mAllData[:, iSecondFeatIdx]
        fCurCorr = pearsonr(vFirstRepr, vSecondRepr)[0]
        # Add edge, if above threshold
        if (fCurCorr > dEdgeThreshold):
            g.add_edge(saFeatures[iFirstFeatIdx], saFeatures[iSecondFeatIdx], weight=round(fCurCorr * 100) / 100)

        # Update queue
        qQueue.task_done()


def getFeatureGraph(mAllData, dEdgeThreshold=0.30, bResetGraph = True, dMinDivergenceToKeep = np.log2(10e5)):

    try:
        if bResetGraph:
            raise Exception("User requested graph recreation.")

        print("Trying to load graph...")
        g = read_multiline_adjlist("graphAdjacencyList.txt")
        with open("usefulFeatureNames.pickle", "rb") as fIn:
            saUsefulFeatureNames = pickle.load(fIn)
        print("Trying to load graph... Done.")
        return g, saUsefulFeatureNames
    except Exception as e:
        print("Trying to load graph... Failed:\n%s\n Recomputing..."%(str(e)))


    # DEBUG LINES
    print("Got data of size %s."%(str(np.shape(mAllData))))
    print("Extracting graph...")
    #############
    # Init graph

    # Determine meaningful features (with a divergence of more than MIN_DIVERGENCE from the control mean)

    iFeatureCount = np.shape(mAllData)[1]
    mMeans = np.nanmean(mAllData, 0) # Ignore nans


    vUseful = [abs(mMeans[iFieldNum]) - dMinDivergenceToKeep > 0.00 for iFieldNum in range(1, iFeatureCount)]

    saFeatures = getFeatureNames()[1:iFeatureCount]
    saUsefulIndices = [ iFieldNum for iFieldNum, _ in enumerate(saFeatures) if vUseful[iFieldNum] ]
    saUsefulFeatureNames = [ saFeatures[iFieldNum] for iFieldNum in saUsefulIndices ]
    iUsefulFeatureCount = len(saUsefulIndices)
    print("Keeping %d features out of %d."%(len(saUsefulIndices), len(saFeatures)))
    ###############################

    g = nx.Graph()
    print("Adding nodes...")
    # Add a node for each feature
    lIndexedNames = enumerate(saFeatures)
    for idx in saUsefulIndices:
        # Only act on useful features
        g.add_node(saFeatures[idx], label = idx)
    print("Adding nodes... Done.")

    # Measure correlations
    print("Creating edges for %d possible pairs..."%(0.5 * (iUsefulFeatureCount * iUsefulFeatureCount)))
    lCombinations = itertools.combinations(saUsefulIndices, 2)

    # Create queue and threads
    qCombination = Queue(10000)
    threads = []
    num_worker_threads = 4
    for i in range(num_worker_threads):
        t = threading.Thread(target=addEdgeAboveThreshold, args=(i, qCombination,))
        t.setDaemon(True)
        t.start()

    iCnt = 0
    dStartTime = clock()
    for iFirstFeatIdx, iSecondFeatIdx in lCombinations:
        qCombination.put((iFirstFeatIdx, iSecondFeatIdx, g, mAllData, saFeatures, iFirstFeatIdx, iSecondFeatIdx, dEdgeThreshold))

        # DEBUG LINES
        if iCnt != 0 and (iCnt % 1000 == 0):
            sys.stdout.write(".")
            if iCnt % 10000 == 0 and (iCnt != 10000):
                dNow = clock()
                dRate = ((dNow - dStartTime) / iCnt)
                dRemaining = (0.5 * (iUsefulFeatureCount * iUsefulFeatureCount) - iCnt) * dRate
                sys.stdout.write("%d (Estimated remaining (sec): %4.2f - Working at a rate of %4.2f pairs/sec)\n"%(iCnt, dRemaining, 1.0 / dRate))


        iCnt += 1
        #############

    print("Waiting for completion...")
    qCombination.join()
    print("Total time (sec): %4.2f"%(clock() - dStartTime))

    print("Creating edges for %d possible pairs... Done."%(0.5 * (iUsefulFeatureCount * iUsefulFeatureCount)))

    print("Extracting graph... Done.")

    print("Removing single nodes... Nodes before removal: %d"%(g.number_of_nodes()))
    toRemove = [curNode for curNode in g.nodes().keys() if len(g[curNode]) == 0]
    while len(toRemove) > 0:
        g.remove_nodes_from(toRemove)
        toRemove = [curNode for curNode in g.nodes().keys() if len(g[curNode]) == 0]
        print("Nodes after removal step: %d" % (g.number_of_nodes()))
    print("Removing single nodes... Done. Nodes after removal: %d"%(g.number_of_nodes()))

    print("Saving graph...")
    write_multiline_adjlist(g, "graphAdjacencyList.txt")
    with open("usefulFeatureNames.pickle", "wb") as fOut:
        pickle.dump(saUsefulFeatureNames, fOut)

    print("Saving graph... Done.")

    print("Trying to load graph... Done.")

    return g, saUsefulFeatureNames


def getGraphAndData(bResetGraph = False, dMinDivergenceToKeep=np.log2(10e6), dEdgeThreshold=0.3):
    mFeatures_noNaNs, vClass = initializeFeatureMatrices(False, True, True)
    gToDraw, saRemainingFeatureNames = getFeatureGraph(mFeatures_noNaNs, dEdgeThreshold=dEdgeThreshold,
                                                       bResetGraph=bResetGraph, dMinDivergenceToKeep=dMinDivergenceToKeep)

    return gToDraw, mFeatures_noNaNs, vClass, saRemainingFeatureNames


def drawGraph(gToDraw):
    plt.figure(figsize=(len(gToDraw.edges()) * 2, len(gToDraw.edges()) * 2))
    plt.clf()
    # pos = graphviz_layout(gToDraw)
    pos = pydot_layout(gToDraw)
    try:
        dNodeLabels = {}
        # For each node
        for nCurNode in gToDraw.nodes():
            # Try to add weight
            dNodeLabels[nCurNode] = "%s (%4.2f)"%(str(nCurNode), gToDraw.node[nCurNode]['weight'])
    except KeyError:
        # Weights could not be added, use nodes as usual
        dNodeLabels = None

    nx.draw_networkx(gToDraw, pos, arrows=False,  node_size=1200, color="blue", with_labels=True, labels=dNodeLabels)
    labels = nx.get_edge_attributes(gToDraw, 'weight')
    nx.draw_networkx_edge_labels(gToDraw, pos, edge_labels=labels)

    plt.show()


def getMeanDegreeCentrality(gGraph):
    mCentralities = list(nx.degree_centrality(gGraph).values())
    return np.mean(mCentralities)

def getAvgShortestPath(gGraph):
    try:
        fAvgShortestPathLength = nx.average_shortest_path_length(gGraph)
    except:
        mShortestPaths = np.asarray([ nx.average_shortest_path_length(g) for g in nx.connected_component_subgraphs(gGraph) ])
        fAvgShortestPathLength = np.mean(mShortestPaths)

    return fAvgShortestPathLength


def getGraphVector(gGraph):
    print("Extracting graph feature vector...")
    mRes = np.asarray(
        [ len(gGraph.edges()), len(gGraph.nodes()), getMeanDegreeCentrality(gGraph), nx.graph_number_of_cliques(gGraph),
             nx.number_connected_components(gGraph), nx.average_node_connectivity(gGraph), getAvgShortestPath(gGraph) ]
    )
    print("Extracting graph feature vector... Done.")
    return mRes


# PCAOnControl()

# PCAOnAllData()

# ClusterAllData()

def spreadingActivation(gGraph, iIterations=100, dPreservationPercent = 0.5, bAbsoluteMass = False):
    print("Applying spreading activation...")
    # In each iteration
    for iIterCnt in range(iIterations):
        # For every node
        for nCurNode in gGraph.nodes():
            # Get max edge weight
            dWeights = np.asarray([ gGraph[nCurNode][nNeighborNode]['weight'] for nNeighborNode in gGraph[nCurNode] ])
            dWeightSum = np.sum(dWeights)

            # For every neighbor
            for nNeighborNode in gGraph[nCurNode]:
                # Get edge percantile weight
                dMassPercentageToMove = gGraph[nCurNode][nNeighborNode]['weight'] / dWeightSum
                try:
                    # Assign part of the weight to the neighbor
                    dMassToMove = (1.0 - dPreservationPercent) * gGraph.node[nCurNode]['weight'] * dMassPercentageToMove
                    # Work with absolute numbers, if requested
                    if bAbsoluteMass:
                        gGraph.node[nNeighborNode]['weight'] = abs(gGraph.node[nNeighborNode]['weight']) +  abs(dMassToMove)
                    else:
                        gGraph.node[nNeighborNode]['weight']+= dMassToMove
                except KeyError:
                    print("Warning: node %s has no weight assigned. Assigning 0."%(str(nCurNode)))
                    gGraph.node[nNeighborNode]['weight'] = 0

            # Reduce my weight equivalently
            gGraph.node[nCurNode]['weight'] *= dPreservationPercent

    print("Applying spreading activation... Done.")
    return gGraph


def assignSampleValuesToGraphNodes(gGraph, mSample, saSampleFeatureNames):
    # For each node
    for nNode in gGraph.nodes():
        # Get corresponding feature idx in sample
        iFeatIdx = saSampleFeatureNames.index(nNode)
        # Assign value of feature as node weight
        dVal = mSample[iFeatIdx]
        # Handle missing values as zero (i.e. non-important)
        if dVal == np.NAN:
            dVal = 0

        gGraph.node[nNode]['weight'] = dVal


def filterGraphNodes(gMainGraph, dKeepRatio):
    # Get all weights
    mWeights = np.asarray([ gMainGraph.node[curNode]['weight'] for curNode in gMainGraph.nodes().keys()])
    # Find appropriate percentile
    dMinWeight = np.percentile(mWeights, (1.0 - dKeepRatio) * 100)
    # Select and remove nodes with lower value
    toRemove = [curNode for curNode in gMainGraph.nodes().keys() if gMainGraph.node[curNode]['weight'] < dMinWeight]
    gMainGraph.remove_nodes_from(toRemove)

    return gMainGraph

def showAndSaveGraph(gToDraw):
    print("Displaying graph...")
    drawGraph(gToDraw)
    print("Displaying graph... Done.")

    print("Saving graph to file...")
    plt.savefig("corrGraph.pdf", bbox_inches='tight')
    print("Saving graph to file... Done.")

def generateAllSampleGraphFeatureVectors(gMainGraph, mAllSamples, saRemainingFeatureNames):
    # For all samples
    # Get the sample vector
    return np.apply_along_axis(lambda  mSample : getSampleGraphFeatureVector(gMainGraph, mSample, saRemainingFeatureNames), 1, mAllSamples)



def getSampleGraphFeatureVector(gMainGraph, mSample, saRemainingFeatureNames):
    # Create a copy of the graph
    gMainGraph = gMainGraph.copy()

    # Assign values
    assignSampleValuesToGraphNodes(gMainGraph, mSample, saRemainingFeatureNames)
    # Apply spreading activation
    gMainGraph = spreadingActivation(gMainGraph, bAbsoluteMass=True)
    # Keep top performer nodes
    gMainGraph = filterGraphNodes(gMainGraph, dKeepRatio=0.25)
    # Extract and return features
    vGraphFeatures = getGraphVector(gMainGraph)
    return vGraphFeatures

def classify(X, y):
    classifier = DecisionTreeClassifier()
    scores = cross_val_score(classifier, X, y, cv=10)
    print ("Avg. Performanace: %4.2f (st. dev. %4.2f) \n %s"%(np.mean(scores), np.std(scores), str(scores)))

    # Output model
    classifier.fit(X, y)
    dot_data = tree.export_graphviz(classifier, out_file=None)
    graph = graphviz.Source(dot_data)
    graph.render("Rules")

def getSampleGraphVectors(gMainGraph, mFeatures_noNaNs, saRemainingFeatureNames, bResetFeatures=True):
    # Get all sample graph vectors
    try:
        print("Trying to load graph feature matrix...")
        if bResetFeatures:
            raise Exception("User requested rebuild of features.")
        with open("graphFeatures.pickle", "rb") as fIn:
            mGraphFeatures = pickle.load(fIn)
        print("Trying to load graph feature matrix... Done.")
    except Exception as e:
        print("Trying to load graph feature matrix... Failed:\n%s"%(str(e)))
        print("Computing graph feature matrix...")

        # TODO: Replace with full set of samples
        mSamplesSelected = np.concatenate((mFeatures_noNaNs[0:40][:], mFeatures_noNaNs[-20:][:]), axis=0)
        print("Extracted selected samples:\n" + str(mSamplesSelected[:][0:10]))
        # Extract vectors
        mGraphFeatures = generateAllSampleGraphFeatureVectors(gMainGraph, mSamplesSelected, saRemainingFeatureNames)
        print("Computing graph feature matrix... Done.")

        print("Saving graph feature matrix...")
        with open("graphFeatures.pickle", "wb") as fOut:
            pickle.dump(mGraphFeatures, fOut)
        print("Saving graph feature matrix... Done.")
    return mGraphFeatures


def main():
    # # main function
    gMainGraph, mFeatures_noNaNs, vClass, saRemainingFeatureNames = getGraphAndData(bResetGraph=False, dEdgeThreshold=0.3, dMinDivergenceToKeep=6)
    # vGraphFeatures = getGraphVector(gMainGraph)
    # print ("Graph feature vector: %s"%(str(vGraphFeatures)))

    # Select a sample
    # mSample = mFeatures_noNaNs[1]
    # vGraphFeatures = getSampleGraphFeatureVector(gMainGraph, mSample, saRemainingFeatureNames)
    # print ("Final graph feature vector: %s"%(str(vGraphFeatures)))

    mGraphFeatures = getSampleGraphVectors(gMainGraph, mFeatures_noNaNs, saRemainingFeatureNames, bResetFeatures=False)

    # Perform PCA
    # TODO: Restore to all instances
    # Get selected instance classes
    vSelectedSamplesClasses = np.concatenate((vClass[0:40][:], vClass[-20:][:]), axis=0)
    # Extract class vector for colors
    aCategories, y = np.unique(vSelectedSamplesClasses, return_inverse=True)
    X, pca3D = getPCA(mGraphFeatures, 3)
    fig = draw3DPCA(X, pca3D, c=y)
    fig.savefig("SelectedSamplesGraphFeaturePCA.pdf")

    classify(X,y)

    # end of main function


# test()
main()

#
#
# def ClassifyInstancesToControlAndTumor():
#     pass
#
#
# ClassifyInstancesToControlAndTumor()
#
# def RepresentSampleAsPearsonCorrelations():
#     # extract mean profile of cancer samples
#     # extract mean profile of control samples
#
#     # every instance is represented based on two features:
#     # <pearson correlation of sample "base" feature vector to mean cancer profile,
#     #  pearson correlation of sample "base" feature vector to mean control profile>
#
# def RepresentSampleAsGraphFeatureVector():
#     pass
#
# def RepresentDataAsGraph():
#     pass
#     # For each DNAmeth feature
#         # Connect high values to high miRNA, mRNA values
#         # Connect high values to low miRNA, mRNA values
#
#
# RepresentDataAsGraph()
