/*******************************************************************************
 * Copyright (c) 2011 Luc Moreau
 *******************************************************************************/
grammar ASN;

options {
  language = Java;
  output=AST;
}

tokens {
    ATTRIBUTE; ATTRIBUTES; RECIPE; START; END; IRI; QNAM; AGENT; ENTITY; ACTIVITY; WGB; USED; WDF; TIME; WDO; WCB; STRING; TYPEDLITERAL; CONTAINER; ID; A; G; U; T; NAMESPACE; DEFAULTNAMESPACE; NAMESPACES; PREFIX; COMPLEMENTARITY; WAW; INT;
}

@header {
  package org.openprovenance.prov.asn;
}

@lexer::header {
  package org.openprovenance.prov.asn;
}


container
	:	'container' 
        (namespaceDeclarations)?
		record*
		'endContainer'

      -> ^(CONTAINER namespaceDeclarations? record*)
	;


namespaceDeclarations :
        (defaultNamespaceDeclaration | namespaceDeclaration) namespaceDeclaration*
        -> ^(NAMESPACES defaultNamespaceDeclaration? namespaceDeclaration*)
    ;

namespaceDeclaration :
        prefix namespace
        -> ^(NAMESPACE prefix namespace)
    ;

/* Problem: NCNAME is a fragment, and failed to be matched.
Put QNAME instead, but that's not correct, it should really be a NCNAME! */

prefix :
       'prefix' QNAME -> ^(PREFIX QNAME)
    ;


namespace :
        IRI_REF
    ;

defaultNamespaceDeclaration :
        'default' IRI_REF
        ->  ^(DEFAULTNAMESPACE IRI_REF)
    ;

record
	:	(entityRecord | activityRecord | agentRecord | generationRecord  | useRecord | derivationRecord | dependenceRecord  | controlRecord | complementarityRecord  |  associationRecord)
	;

entityRecord
	:	'entity' '(' identifier optionalAttributeValuePairs ')'
        -> ^(ENTITY identifier optionalAttributeValuePairs)
	;


activityRecord
	:	'activity' '(' identifier (',' recipeLink)? ',' (startTime)? ',' (endTime)? optionalAttributeValuePairs ')'
        -> ^(ACTIVITY identifier ^(RECIPE recipeLink?) ^(START startTime?) ^(END endTime?) optionalAttributeValuePairs )
	;

agentRecord
	:	'agent' '(' identifier optionalAttributeValuePairs	')' -> ^(AGENT identifier optionalAttributeValuePairs )
	;

generationRecord
	:	'wasGeneratedBy' '('  optionalIdentifier identifier ',' identifier optionalTime optionalAttributeValuePairs ')'
      -> ^(WGB optionalIdentifier identifier+ optionalTime optionalAttributeValuePairs)
	;

useRecord
	:	'used' '(' optionalIdentifier identifier ',' identifier optionalTime optionalAttributeValuePairs ')'
      -> ^(USED optionalIdentifier identifier+ optionalTime optionalAttributeValuePairs)
	;

derivationRecord
	:	'wasDerivedFrom' '(' id2=identifier ',' id1=identifier (',' a=identifier ',' g2=identifier ',' u1=identifier )?	optionalAttributeValuePairs ')'
      -> ^(WDF $id2 $id1 ^(A $a?)  ^(G $g2?) ^(U $u1?) optionalAttributeValuePairs)
	;

optionalAttributeValuePairs
    :
    (',' '[' attributeValuePairs ']')?
        -> ^(ATTRIBUTES attributeValuePairs?)
    ;

optionalTime
    :
    (',' time )?
        -> ^(TIME time?)
    ;

optionalIdentifier
    :
    (identifier ',')?
        -> ^(ID identifier?)
    ;

dependenceRecord
	:	'dependedUpon' '(' optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(WDO optionalIdentifier identifier+ optionalAttributeValuePairs)
	;

controlRecord
	:	'wasControlledBy' '(' optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(WCB optionalIdentifier identifier+ optionalAttributeValuePairs)
	;

complementarityRecord
	:	'wasComplementOf' '('  optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(COMPLEMENTARITY optionalIdentifier identifier+ optionalAttributeValuePairs)
	;


associationRecord
	:	'wasAssociatedWith' '(' optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(WAW optionalIdentifier identifier+ optionalAttributeValuePairs)
	;

identifier
	:
        QNAME -> ^(ID QNAME)
	;

attribute
	:
        QNAME
	;

attributeValuePairs
	:
        (  | attributeValuePair ( ','! attributeValuePair )* )
	;


attributeValuePair
	:
        attribute '=' literal  -> ^(ATTRIBUTE attribute literal)
	;


time
	:
        xsdDateTime
	;
startTime
	:
        xsdDateTime
	;
endTime
	:
        xsdDateTime
	;

recipeLink
	:
        IRI_REF
	;

/* TODO: complete grammar of Literal */
literal
	:
        (STRINGLITERAL -> ^(STRING STRINGLITERAL) |
         INTLITERAL -> ^(INT INTLITERAL) |
         STRINGLITERAL '%%' datatype -> ^(TYPEDLITERAL STRINGLITERAL datatype))
	;

datatype
	:
        (IRI_REF -> ^(IRI IRI_REF)
        |
         QNAME -> ^(QNAM QNAME))
	;
	
QNAME	
	:	NCNAME (':' NCNAME)?  
	;


fragment CHAR
	: ('\u0009' | '\u000A' | '\u000D' | '\u0020'..'\uD7FF' | '\uE000'..'\uFFFD' )
	;

/* fragment DIGITS 	   
	: ('0'..'9')+
	;
*/

fragment NCNAMESTARTCHAR
	: ('A'..'Z') | '_' | ('a'..'z') | ('\u00C0'..'\u00D6') | ('\u00D8'..'\u00F6') | ('\u00F8'..'\u02FF') | ('\u0370'..'\u037D') | ('\u037F'..'\u1FFF') | ('\u200C'..'\u200D') | ('\u2070'..'\u218F') | ('\u2C00'..'\u2FEF') | ('\u3001'..'\uD7FF') | ('\uF900'..'\uFDCF') | ('\uFDF0'..'\uFFFD')
	;
	
fragment NCNAMECHAR
	:   	NCNAMESTARTCHAR | '-' | '.' | '0'..'9' | '\u00B7' | '\u0300'..'\u036F' | '\u203F'..'\u2040'
	;
	
fragment NAMECHAR	   
	:   ':' 
	| NCNAMECHAR
	;
	
fragment NAMESTARTCHAR
	:  ':' 
	| NCNAMESTARTCHAR
	;
	
	
fragment NCNAME	           
	:  NCNAMESTARTCHAR NCNAMECHAR*
	;	


NCNAME_COLON_STAR
	: NCNAME ':' '*'
	;
STAR_COLON_NCNAME
	: '*' ':' NCNAME;

/*
NUMERICLITERAL 	   
	: ( ('.' DIGITS) |(DIGITS ('.' ('0'..'9')*)?)) (('e'|'E') ('+'|'-')? DIGITS)?
	;
		
*/
fragment QUOTE	           
	: '"'
	;
	
fragment APOS		   
	: '\''
	;
	
fragment ESCAPEQUOTE 	   
	: QUOTE QUOTE
	;
	
	
fragment ESCAPEAPOS 	   
	: APOS APOS
	;
	
fragment CHARNOQUOTE	   
	: ~(~CHAR | QUOTE)
	;
	
	
fragment CHARNOAPOS	   
	: ~(~CHAR | APOS)
	;


STRINGLITERAL		   
	: (QUOTE (ESCAPEQUOTE | CHARNOQUOTE)* QUOTE) 
	| (APOS  (ESCAPEAPOS | CHARNOAPOS)* APOS)
	;
			 

/* 
This lexer rule for comments handles multiline, nested comments
*/
COMMENT_CONTENTS
        :       '(:'
                {
                        $channel=98;
                }
                (       ~('('|':')
                        |       ('(' ~':') => '('
                        |       (':' ~')') => ':'
                        |       COMMENT_CONTENTS
                )*
                ':)'
        ;


WS		
	: (' '|'\r'|'\t'|'\u000C'|'\n')+ {$channel = HIDDEN;}
	;


IRI_REF
  :
  LESS
  ( options {greedy=false;}:
    ~(
      LESS
      | GREATER
      | '"'
      | OPEN_CURLY_BRACE
      | CLOSE_CURLY_BRACE
      | '|'
      | '^'
      | '\\'
      | '`'
      | ('\u0000'..'\u0020')
     )
  )*
  GREATER
  ;


LESS
  :
  '<'
  ;

GREATER
  :
  '>'
  ;
OPEN_CURLY_BRACE
  :
  '{'
  ;

CLOSE_CURLY_BRACE
  :
  '}'
  ;




xsdDateTime: IsoDateTime;




IsoDateTime: (DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT 'T' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ('.' DIGIT (DIGIT DIGIT?)?)? ('Z' | TimeZoneOffset)?)
    ;

fragment DIGIT: '0'..'9';

INTLITERAL:
    '-'? ('0'..'9')+
    ;


TimeZoneOffset: ('+' | '-') DIGIT DIGIT ':' DIGIT DIGIT;




