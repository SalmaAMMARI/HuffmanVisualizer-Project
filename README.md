# HuffmanVisualizer Project

A visual and interactive implementation of the **Huffman Coding Algorithm** using **JavaFX**.

---

## 📖 Overview

Huffman Coding is a **lossless compression algorithm** that assigns **shorter codes to frequent characters** and **longer codes to less frequent ones**.  
This project implements the full algorithm *and* provides a **graphical visualization** of each step.

---

## 🎯 Project Concept

The algorithm works through:

1. **Frequency Analysis** – Count the frequency of each character.  
2. **Tree Construction** – Build a binary tree where leaves represent characters.  
3. **Code Generation** – Traverse the tree (0 = left, 1 = right).  
4. **Compression** – Replace text characters with Huffman codes.  
5. **Decompression** – Decode binary data using the reconstructed tree.

The visualizer shows each construction step in real-time.

---

## 🏗️ Model Classes

### **1. HuffmanNode.java**
Represents a single node in the Huffman tree.

**Attributes**
- `character` – Stored character (null for internal nodes)  
- `frequency` – How often the character appears  
- `left`, `right` – Child nodes  

**Key Methods**
- `isLeaf()` – True if it has no children  
- `toString()` – Debug printing  

---

### **2. HuffmanTree.java**
Represents the entire tree structure.

**Attributes**
- `root` – Root node of the tree  
- `constructionSteps` – Saved snapshots of tree-building  
- `code` – Map of character → Huffman code  

**Key Methods**
- `generateMycode()` – Builds all codes  
- `getConstructionSteps()` – Access construction history  
- `addConstructionSteps()` – Save a step  

---

## 🔧 Algorithm Classes

### **1. FrequencyCounter.java**
Counts character frequencies.  
- `getFrequency(List<Character>)` → HashMap<Character, Integer>

### **2. TextToListOfCharacter.java**
Converts a string into a character list.  
- `TextFormatModifier(String)` → List<Character>

### **3. SortedNodesCreator.java**
Builds a sorted node list.  
- `sort(String)` → sorted List<HuffmanNode>

### **4. MergeSort.java**
Sorts nodes by frequency using merge sort.  
- `mergeSort(List<HuffmanNode>)`  
- `merge()`

### **5. TreeBuilder.java**
Builds the Huffman tree.  
- `buildHuffmanTree(String)` → HuffmanTree

### **6. CodeGenerator.java**
Generates Huffman binary codes.  
- `generateCode()` – DFS traversal

### **7. Compressor.java**
Encodes a text string.  
- `compress(String, HuffmanTree)` → binary string

### **8. Decompressor.java**
Decodes binary text.  
- `decompress(String, HuffmanTree)` → original text

---

## 🎨 User Interface (JavaFX)

### **HuffmanTreeVisualizer.java**
Features include:

- Load text files  
- Step-by-step tree construction  
- Animated path tracing  
- Auto-play mode  
- Compression statistics  
- Code table view  
- Export encoded text  

---

## 🚀 How to Run

---

## 📌 Prerequisites

- **Java 8 or higher** (Java 11+ recommended)  
- **JavaFX SDK installed** (bundled with many recent JDKs)  
- **Any Java IDE**: IntelliJ IDEA, Eclipse, NetBeans, or VS Code with Java extensions  

---

## 🖥️ Running in an IDE

### 🔹 IntelliJ IDEA
1. Open the project in IntelliJ IDEA  
2. Navigate to: `src/huffman/ui/HuffmanTreeVisualizer.java`  
3. Right-click → **Run 'HuffmanTreeVisualizer.main()'**  
4. If JavaFX isn’t configured, IntelliJ will automatically prompt you to add the module path  

## ⚡ Quick Start

Just open the project in your favorite IDE and run.




