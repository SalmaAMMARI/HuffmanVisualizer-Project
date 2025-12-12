# HuffmanVisualizer-Project
Huffman Coding Algorithm - Visualizer Project
📖 Overview
Huffman Coding is a lossless data compression algorithm that assigns variable-length codes to input characters, with shorter codes assigned to more frequent characters and longer codes to less frequent ones. This project implements a complete Huffman coding system with a graphical visualization interface in JavaFX.

🎯 Project Concept
The Huffman algorithm works by:

Frequency Analysis: Counting the frequency of each character in the input text

Tree Construction: Building a binary tree where each leaf represents a character and its frequency

Code Generation: Traversing the tree to assign binary codes (0 for left, 1 for right)

Compression: Replacing characters with their Huffman codes

Decompression: Using the tree to decode the binary string back to original text

This project not only implements the algorithm but also provides step-by-step visualization of the tree construction process.

📁 Project Structure
text
huffman/
├── algorithm/          # Core algorithm implementations
│   ├── FrequencyCounter.java
│   ├── TextToListOfCharacter.java
│   ├── CodeGenerator.java
│   ├── Compressor.java
│   ├── Decompressor.java
│   ├── TreeBuilder.java
│   ├── SortedNodesCreator.java
│   ├── MergeSort.java
│   ├── Dfs.java
│   └── Test.java
├── model/             # Data models
│   ├── HuffmanTree.java
│   └── HuffmanNode.java
└── ui/                # User interface
    └── HuffmanTreeVisualizer.java
🏗️ Model Classes
1. HuffmanNode.java
The fundamental building block representing a node in the Huffman tree:

Attributes:

character: The character stored in this node (null for internal nodes)

frequency: The frequency/count of the character

left: Reference to left child node

right: Reference to right child node

Key Methods:

isLeaf(): Returns true if node has no children

toString(): String representation for debugging

2. HuffmanTree.java
Represents the complete Huffman tree structure:

Attributes:

root: The root node of the Huffman tree

constructionSteps: List of intermediate states during tree construction

code: HashMap mapping characters to their Huffman codes

Key Methods:

generateMycode(): Generates Huffman codes for all characters

getConstructionSteps(): Returns all intermediate tree states

addConstructionSteps(): Records a construction step

🔧 Algorithm Classes
1. FrequencyCounter.java
Counts character frequencies in input text:

getFrequency(List<Character> text): Returns HashMap<Character, Integer> with character frequencies

2. TextToListOfCharacter.java
Converts input string to list of characters:

TextFormatModifier(String text): Converts string to List<Character>

3. SortedNodesCreator.java
Creates sorted list of HuffmanNode objects:

sort(String initial_text): Returns sorted list of nodes by frequency

4. MergeSort.java
Implements merge sort algorithm for sorting nodes:

mergeSort(List<HuffmanNode> A): Sorts nodes by frequency ascending

merge(): Merges two sorted lists

5. TreeBuilder.java
Core algorithm for building Huffman tree:

buildHuffmanTree(String text): Constructs complete Huffman tree with step tracking

6. CodeGenerator.java
Generates Huffman codes from tree:

generateCode(): Recursively traverses tree to assign binary codes

7. Compressor.java
Compresses text using generated Huffman codes:

compress(String text, HuffmanTree tree): Converts text to binary string

8. Decompressor.java
Decompresses encoded text:

decompress(String encodedText, HuffmanTree tree): Converts binary back to text

🎨 User Interface (JavaFX)
HuffmanTreeVisualizer.java
A comprehensive visualization tool with features:

File Loading: Load text files for compression

Step-by-Step Visualization: Watch the tree construction process

Path Animation: Visualize encoding paths for specific characters

Auto-Play: Automatic demonstration of all character encodings

Compression Statistics: Display compression ratios and savings

Code Table: Show complete character-to-code mappings

Export Options: Copy encoded text to clipboard

🚀 How to Run
Prerequisites
Java 8 or higher

JavaFX SDK

Compilation and Execution
bash
# Compile all Java files
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls huffman/**/*.java

# Run the visualizer
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls huffman.ui.HuffmanTreeVisualizer

# Or run the test program
java huffman.algorithm.Test
📊 Example Usage
Load a text file or use default text

Observe tree construction step by step

View final Huffman tree with character codes

Select characters to see their encoding paths

View compression statistics and encoded text

Export results for use in other applications

🎯 Key Features
Educational Visualization: Perfect for understanding Huffman algorithm

Interactive Learning: Step-through construction and path tracing

Real Compression: Actual encoding/decoding implementation

File Support: Load and process any text file

Performance Stats: Compression ratio calculations

Professional UI: Clean, modern JavaFX interface

📈 Algorithm Complexity
Time Complexity: O(n log n) for tree construction

Space Complexity: O(n) for storing frequency table and tree

Optimality: Produces optimal prefix codes for given frequencies

🔍 Learning Outcomes
This project demonstrates:

Tree data structures and their applications

Priority queue concepts through sorted lists

Recursive algorithms for tree traversal

File I/O operations in Java

JavaFX GUI development

Algorithm visualization techniques

Lossless compression principles

🤝 Contributing
Feel free to fork this project and submit pull requests with:

Additional visualization features

Performance optimizations

Support for binary file compression

Multilingual character support

Web-based interface
