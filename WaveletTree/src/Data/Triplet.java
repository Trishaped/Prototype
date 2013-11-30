package Data;
import Jena_Mock.*;

/**
 * Only for test! must be replaced by jena...
 * pour jena: (class FrontsTriple)
 * 
 * NOTE:
 * 
 * Triplet:        Property
 *      Resource ------------>> RDFNode 
 * 
 * Hierarchie:
 *      RDFNode
 *         ^
 *      Resource  
 *         ^
 *      Property
 */
public class Triplet {
    Resource  subject;
    Property  predicate;
    RDFNode   object;
}
