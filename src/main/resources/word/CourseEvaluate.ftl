[#ftl]
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<?mso-application progid="Word.Document"?>
<pkg:package 
    xmlns:pkg="http://schemas.microsoft.com/office/2006/xmlPackage">
    <pkg:part pkg:name="/_rels/.rels" pkg:contentType="application/vnd.openxmlformats-package.relationships+xml" pkg:padding="512">
        <pkg:xmlData>
            <Relationships 
                xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
                <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/>
                <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/>
            </Relationships>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/_rels/document.xml.rels" pkg:contentType="application/vnd.openxmlformats-package.relationships+xml" pkg:padding="256">
        <pkg:xmlData>
            <Relationships 
                xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/>
                <Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings" Target="webSettings.xml"/>
                <Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/footnotes" Target="footnotes.xml"/>
                <Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/endnotes" Target="endnotes.xml"/>
                <Relationship Id="rId7" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image1.png"/>
                <Relationship Id="rId8" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/header" Target="header1.xml"/>
                <Relationship Id="rId9" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable" Target="fontTable.xml"/>
                <Relationship Id="rId10" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme" Target="theme/theme1.xml"/>
                <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/numbering" Target="numbering.xml"/>
                <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
            </Relationships>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/document.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml">
        <pkg:xmlData>
            <w:document mc:Ignorable="w14 w15 wp14" 
                xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" 
                xmlns:mo="http://schemas.microsoft.com/office/mac/office/2008/main" 
                xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" 
                xmlns:mv="urn:schemas-microsoft-com:mac:vml" 
                xmlns:o="urn:schemas-microsoft-com:office:office" 
                xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" 
                xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" 
                xmlns:v="urn:schemas-microsoft-com:vml" 
                xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" 
                xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" 
                xmlns:w10="urn:schemas-microsoft-com:office:word" 
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" 
                xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" 
                xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml" 
                xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" 
                xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" 
                xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" 
                xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape">
                <w:body>
                    [#if data??]
                    [#assign indicationMap = {} gradecomposeAndIndicationMap = {} courseGradecomposeIndicationMapOrigin = {}]
                    [#if data.indicationMap??]
                        [#assign indicationMap = data.indicationMap]
                    [/#if]
                    [#if data.gradecomposeAndIndicationMap??]
                        [#assign gradecomposeAndIndicationMap = data.gradecomposeAndIndicationMap]
                    [/#if]
                    [#if data.courseGradecomposeIndicationMap??]
                        [#assign courseGradecomposeIndicationMapOrigin = data.courseGradecomposeIndicationMap]
                    [/#if]
                    <w:p w14:paraId="6283C845" w14:textId="77777777" w:rsidR="0044668B" w:rsidRPr="0044668B" w:rsidRDefault="00F303A5" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="00F303A5">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>[#if data.startYear??]${data.startYear?c}[/#if]-[#if data.endYear??]${data.endYear?c}[/#if]</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> </w:t>
                        </w:r>
                        <w:r w:rsidR="0044668B" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>学年《</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>${data.courseName!""}</w:t>
                        </w:r>
                        <w:r w:rsidR="0044668B" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>》课程目标达成度评价材料</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="74A6FB4B" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="45037298" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="304FFEEC" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="001E54A1">
                        <w:pPr>
                            <w:ind w:left="1080" w:firstLine="420"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>教学班级：</w:t>
                        </w:r>
                        <w:r w:rsidR="00AD4E21">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>[#if data.grade??]${data.grade?c}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidR="00AD4E21">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>级，</w:t>
                        </w:r>
                        [#if data.educlassNames??]
                        <w:r w:rsidR="00AD4E21">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>${data.educlassNames!''}</w:t>
                        </w:r>
                        [/#if]
                        <w:r w:rsidR="00AD4E21">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>班</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="07F36513" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="1F794C5E" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="00AD4E21">
                        <w:pPr>
                            <w:ind w:firstLineChars="500" w:firstLine="1500"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>课程</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>类别</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>：</w:t>
                        </w:r>
                        <w:r w:rsidR="00AD4E21">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>${data.courseType!""}</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="0B736A0C" w14:textId="77777777" w:rsidR="0044668B" w:rsidRPr="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="65B8152A" w14:textId="77777777" w:rsidR="0044668B" w:rsidRPr="0044668B" w:rsidRDefault="0044668B" w:rsidP="00AD4E21">
                        <w:pPr>
                            <w:ind w:firstLineChars="500" w:firstLine="1500"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>评价人：</w:t>
                        </w:r>
                        <w:r w:rsidR="0043515E">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t></w:t>
                        </w:r>
                        <w:r w:rsidR="0043515E">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t></w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="6210DF0C" w14:textId="77777777" w:rsidR="0044668B" w:rsidRPr="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="3C49B3B0" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="727F4917" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="1823F59D" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="36923E0F" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="23639DDE" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="3FAC0E03" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="46A2640D" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="5EE53431" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="1A10241B" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="7845CD49" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="28E1738A" w14:textId="77777777" w:rsidR="0044668B" w:rsidRPr="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="6C1A3F16" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="00AD4E21" w:rsidP="00A51DF9">
                        <w:pPr>
                            <w:ind w:firstLineChars="1300" w:firstLine="3900"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>    </w:t>
                        </w:r>
                        <w:r w:rsidR="0044668B" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>年</w:t>
                        </w:r>
                        <w:r w:rsidR="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t xml:space="preserve">  </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>  </w:t>
                        </w:r>
                        <w:r w:rsidR="0044668B" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="30"/>
                                <w:szCs w:val="30"/>
                            </w:rPr>
                            <w:t>月</w:t>
                        </w:r>
                        <w:r w:rsidR="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:br w:type="page"/>
                        </w:r>
                        <w:r w:rsidR="0044668B" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:lastRenderedPageBreak/>
                            <w:t>一、课程考核方式、内容与毕业要求指标点对应关系</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="45F3138C" w14:textId="77777777" w:rsidR="0044668B" w:rsidRPr="0044668B" w:rsidRDefault="0044668B" w:rsidP="003D6DD9">
                        <w:pPr>
                            <w:ind w:firstLineChars="200" w:firstLine="480"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>本课程考核严格按教学大纲考核要求进行考核。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="1941B2B6" w14:textId="77777777" w:rsidR="0044668B" w:rsidRPr="0044668B" w:rsidRDefault="0044668B" w:rsidP="003D6DD9">
                        <w:pPr>
                            <w:ind w:firstLineChars="200" w:firstLine="480"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>计分制：百分制（</w:t>
                        </w:r>
                        [#if data.percentSystem?? && data.percentSystem]
                        <w:r w:rsidR="0070608D">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>√</w:t>
                        </w:r>
                        [/#if]
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>）；五级分制（</w:t>
                        </w:r>
                        [#if data.fiveGradeSystem?? && data.fiveGradeSystem]
                            <w:r w:rsidR="0070608D">
                                <w:rPr>
                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                    <w:sz w:val="24"/>
                                </w:rPr>
                                <w:t>√</w:t>
                            </w:r>
                        [/#if]
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>）；两级分制（</w:t>
                        </w:r>
                        [#if data.twoGradeSystem?? && data.twoGradeSystem]
                            <w:r w:rsidR="0070608D">
                                <w:rPr>
                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                    <w:sz w:val="24"/>
                                </w:rPr>
                                <w:t>√</w:t>
                            </w:r>
                        [/#if]
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>）</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="0871A339" w14:textId="77777777" w:rsidR="0044668B" w:rsidRPr="0044668B" w:rsidRDefault="0044668B" w:rsidP="003D6DD9">
                        <w:pPr>
                            <w:ind w:firstLineChars="200" w:firstLine="480"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>本课程成绩由</w:t>
                        </w:r>
                        <w:r w:rsidR="0070608D" w:rsidRPr="0070608D">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>[#if data.ccCourseGradecomposes??][#list data.ccCourseGradecomposes as gradeCompose]${gradeCompose.gradecomposeName}[#if gradeCompose?has_next]、[/#if][/#list][/#if]</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t xml:space="preserve">组合而成。其中</w:t>
                        </w:r>
                        [#if data.ccCourseGradecomposes??]
                        [#list data.ccCourseGradecomposes as gradeCompose]
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t xml:space="preserve">${gradeCompose.gradecomposeName}占 </w:t>
                        </w:r>
                        <w:r w:rsidR="00AD4E21">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>${gradeCompose.percentage}</w:t>
                        </w:r>
                        <w:r w:rsidR="0070608D" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> %</w:t>
                        </w:r>
                        [#if gradeCompose?has_next]
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>，</w:t>
                        </w:r>
                        [/#if]
                        [/#list]
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>。</w:t>
                        </w:r>
                        [/#if]
                    </w:p>
                    <w:p w14:paraId="0A8F708A" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="0044668B" w:rsidP="003D6DD9">
                        <w:pPr>
                            <w:ind w:firstLineChars="200" w:firstLine="480"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>课程考核内容和毕业要求指标点对应关系和目标分值见表1。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="3BDD25C4" w14:textId="77777777" w:rsidR="0044668B" w:rsidRPr="0044668B" w:rsidRDefault="0044668B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="2DFE9C8E" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C5116B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>表1</w:t>
                        </w:r>
                        <w:r w:rsidR="00057987" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve">    </w:t>
                        </w:r>
                        <w:r w:rsidR="00626761">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>[#if data.startYear??]${data.startYear?c}[/#if]-[#if data.endYear??]${data.endYear?c}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidR="00626761" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> 学年《</w:t>
                        </w:r>
                        <w:r w:rsidR="00626761">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.courseName!""}</w:t>
                        </w:r>
                        <w:r w:rsidR="00626761" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>》课程考核内容结构与毕业要求指标点对应关系</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblW w:w="0" w:type="auto"/>
                            <w:jc w:val="center"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:sz="18" w:space="0" w:color="auto"/>
                                <w:left w:val="single" w:sz="18" w:space="0" w:color="auto"/>
                                <w:bottom w:val="single" w:sz="18" w:space="0" w:color="auto"/>
                                <w:right w:val="single" w:sz="18" w:space="0" w:color="auto"/>
                                <w:insideH w:val="single" w:sz="2" w:space="0" w:color="auto"/>
                                <w:insideV w:val="single" w:sz="2" w:space="0" w:color="auto"/>
                            </w:tblBorders>
                            <w:tblLook w:val="01E0" w:firstRow="1" w:lastRow="1" w:firstColumn="1" w:lastColumn="1" w:noHBand="0" w:noVBand="0"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="1041"/>
                            <w:gridCol w:w="3857"/>
                            <w:gridCol w:w="1938"/>
                            <w:gridCol w:w="1686"/>
                        </w:tblGrid>
                        <w:tr w:rsidR="00810440" w:rsidRPr="0044668B" w14:paraId="6D928105" w14:textId="77777777" w:rsidTr="009B07C4">
                            <w:trPr>
                                <w:trHeight w:val="335"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="4898" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="1BC8D0F3" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="0044668B" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>课程支撑的毕业要求</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>及指标点</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1938" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="0204BA32" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="0044668B" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>考核具体内容</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w14:paraId="0A67C152" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="0044668B" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>（</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>试卷</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>/平时</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>表现/实验</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1686" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="49702043" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="0044668B" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>目标分值</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="00810440" w:rsidRPr="0044668B" w14:paraId="40060865" w14:textId="77777777" w:rsidTr="009B07C4">
                            <w:trPr>
                                <w:trHeight w:val="294"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1041" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="286C93CE" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="004F4036" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>毕业要求</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3857" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="686F0A65" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="0044668B" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>指标点</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1938" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="06EEA640" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="0044668B" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1686" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="0C6CABC3" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="0044668B" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#if data.ccCourseGradecomposeIndications??]
                        [#list data.ccCourseGradecomposeIndications as courseGradecompossIndicationMap]
                            [#assign indicationInfo = {} graduateIndexNum = -1 indicationIndexNum = -1 indicationContent = "" targetGradetotal = 0]
                            [#if courseGradecompossIndicationMap.indicationId?? && indicationMap?? && indicationMap?size>0]
                                [#assign indicationInfo = indicationMap[courseGradecompossIndicationMap.indicationId]]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.graduateIndexNum??]
                                [#assign graduateIndexNum = indicationInfo.graduateIndexNum]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.indicationIndexNum??]
                                [#assign indicationIndexNum = indicationInfo.indicationIndexNum]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.indicationContent??]
                                [#assign indicationContent = indicationInfo.indicationContent]
                            [/#if]
                        [#if courseGradecompossIndicationMap.gradecomposeIndications?? && courseGradecompossIndicationMap.gradecomposeIndications?size > 0]
                        [#list courseGradecompossIndicationMap.gradecomposeIndications as gradecompossIndication]
                        [#if gradecompossIndication?is_first]
                        <w:tr w:rsidR="00810440" w:rsidRPr="007051F3" w14:paraId="4DBDB9A4" w14:textId="77777777" w:rsidTr="009B07C4">
                            <w:trPr>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1041" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:tcBorders>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="427CD924" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00810440">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>毕业要求[#if graduateIndexNum?? && (graduateIndexNum>=0)]${graduateIndexNum}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3857" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:tcBorders>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="4AA31ABC" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00810440">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if graduateIndexNum?? && (graduateIndexNum>=0)]${graduateIndexNum}[/#if][#if indicationIndexNum?? && (indicationIndexNum >= 0)].${indicationIndexNum}[/#if][#if indicationContent??]${indicationContent}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1938" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="6E458446" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00810440">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.gradecomposeName??]${gradecompossIndication.gradecomposeName}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1686" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="7BF0536F" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00810440">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.maxScore??]${gradecompossIndication.maxScore}[/#if]*[#if gradecompossIndication.weight??]${gradecompossIndication.weight}[/#if]（[#if gradecompossIndication.maxScore?? && gradecompossIndication.weight??]${gradecompossIndication.maxScore*gradecompossIndication.weight}[#assign targetGradetotal=(targetGradetotal+(gradecompossIndication.maxScore*gradecompossIndication.weight))][/#if]分）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#else]
                        <w:tr w:rsidR="00810440" w:rsidRPr="007051F3" w14:paraId="0C34927D" w14:textId="77777777" w:rsidTr="009B07C4">
                            <w:trPr>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1041" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:tcBorders>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="0287AD36" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3857" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:tcBorders>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="2A0CA1EC" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1938" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="41ACA225" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00810440">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.gradecomposeName??]${gradecompossIndication.gradecomposeName}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1686" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="16D533E5" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00810440">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.maxScore??]${gradecompossIndication.maxScore}[/#if]*[#if gradecompossIndication.weight??]${gradecompossIndication.weight}[/#if]（[#if gradecompossIndication.maxScore?? && gradecompossIndication.weight??]${gradecompossIndication.maxScore*gradecompossIndication.weight}[#assign targetGradetotal=(targetGradetotal+(gradecompossIndication.maxScore*gradecompossIndication.weight))][/#if]分）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [/#if]
                        [/#list]
                        <w:tr w:rsidR="00810440" w:rsidRPr="007051F3" w14:paraId="0C34927D" w14:textId="77777777" w:rsidTr="009B07C4">
                            <w:trPr>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1041" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:tcBorders>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="0287AD36" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3857" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:tcBorders>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="2A0CA1EC" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1938" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="41ACA225" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00810440">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>合计</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1686" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="16D533E5" w14:textId="77777777" w:rsidR="00810440" w:rsidRPr="00810440" w:rsidRDefault="00810440" w:rsidP="009B07C4">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00810440">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if targetGradetotal?? && (targetGradetotal>0)]${targetGradetotal}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [/#if]
                        [/#list]
                        [/#if]
                    </w:tbl>
                    <w:p w14:paraId="70B33563" w14:textId="77777777" w:rsidR="00810440" w:rsidRDefault="00810440" w:rsidP="004F4036">
                        <w:pPr>
                            <w:ind w:firstLineChars="100" w:firstLine="180"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="26141871" w14:textId="77777777" w:rsidR="004F4036" w:rsidRDefault="004F4036" w:rsidP="004F4036">
                        <w:pPr>
                            <w:ind w:firstLineChars="100" w:firstLine="180"/>
                            <w:rPr>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>说明：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>毕业要求</w:t>
                        </w:r>
                        <w:r w:rsidR="00AD4E21">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>及</w:t>
                        </w:r>
                        <w:r w:rsidRPr="004F4036">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>指标点</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>参照培养计划及</w:t>
                        </w:r>
                        <w:r w:rsidRPr="004F4036">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>课程达成度权重系数表</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>，</w:t>
                        </w:r>
                        <w:r w:rsidR="005226C1">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>考核具体内容要求填写试卷题号、大作业名称、平时成绩项名；</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>该课程毕业要求</w:t>
                        </w:r>
                        <w:r w:rsidR="005226C1">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>所有</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>指标点</w:t>
                        </w:r>
                        <w:r w:rsidR="004E1209">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>期末（试卷或大作业）和平时成绩</w:t>
                        </w:r>
                        <w:r w:rsidR="005226C1">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>等</w:t>
                        </w:r>
                        <w:r w:rsidR="00306DEE">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>满分</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>为</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>100</w:t>
                        </w:r>
                        <w:r w:rsidR="004E1209">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>，目标分值填写方法：该指标点分配分数</w:t>
                        </w:r>
                        <w:r w:rsidR="004E1209">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>*</w:t>
                        </w:r>
                        <w:r w:rsidR="004E1209">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>期末成绩比例</w:t>
                        </w:r>
                        <w:r w:rsidR="004E1209" w:rsidRPr="004E1209">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>/</w:t>
                        </w:r>
                        <w:r w:rsidR="004E1209">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>该指标点分配分数</w:t>
                        </w:r>
                        <w:r w:rsidR="004E1209">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>*</w:t>
                        </w:r>
                        <w:r w:rsidR="004E1209">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>平时成绩比例</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="6BFA7C18" w14:textId="77777777" w:rsidR="00057987" w:rsidRDefault="00057987" w:rsidP="004F4036">
                        <w:pPr>
                            <w:widowControl/>
                            <w:adjustRightInd w:val="0"/>
                            <w:ind w:firstLineChars="200" w:firstLine="360"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="037784C8" w14:textId="77777777" w:rsidR="00AD5994" w:rsidRDefault="00057987" w:rsidP="00AD5994">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:br w:type="page"/>
                        </w:r>
                        <w:r w:rsidR="00AD5994" w:rsidRPr="004E2DC6">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:bCs/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:lastRenderedPageBreak/>
                            <w:t>二、 本课程考核评价依据合理性审核</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="1B2D9B0A" w14:textId="77777777" w:rsidR="003D6DD9" w:rsidRPr="003D6DD9" w:rsidRDefault="003D6DD9" w:rsidP="003D6DD9">
                        <w:pPr>
                            <w:ind w:firstLineChars="200" w:firstLine="480"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>根据本课程考核内容是否完整体现在对相应毕业要求的指标点考核在试题难度、分值和覆盖面等的合理性审核，见表2</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="3FA192D1" w14:textId="77777777" w:rsidR="00AD5994" w:rsidRDefault="00AD5994" w:rsidP="00F07789">
                        <w:pPr>
                            <w:widowControl/>
                            <w:adjustRightInd w:val="0"/>
                            <w:ind w:firstLineChars="200" w:firstLine="360"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="348A4101" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="0044668B">
                        <w:pPr>
                            <w:widowControl/>
                            <w:adjustRightInd w:val="0"/>
                            <w:ind w:firstLineChars="200" w:firstLine="420"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>表</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve">2 </w:t>
                        </w:r>
                        <w:r w:rsidR="00057987" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve">   </w:t>
                        </w:r>
                        <w:r w:rsidR="000E1EA9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>[#if data.startYear??]${data.startYear?c}[/#if]-[#if data.endYear??]${data.endYear?c}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>学年《</w:t>
                        </w:r>
                        <w:r w:rsidR="000E1EA9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.courseName!""}</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>》课程考核合理性审核表</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblW w:w="0" w:type="auto"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:sz="18" w:space="0" w:color="auto"/>
                                <w:left w:val="single" w:sz="18" w:space="0" w:color="auto"/>
                                <w:bottom w:val="single" w:sz="18" w:space="0" w:color="auto"/>
                                <w:right w:val="single" w:sz="18" w:space="0" w:color="auto"/>
                                <w:insideH w:val="single" w:sz="6" w:space="0" w:color="auto"/>
                                <w:insideV w:val="single" w:sz="6" w:space="0" w:color="auto"/>
                            </w:tblBorders>
                            <w:tblLook w:val="00A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="0"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="1908"/>
                            <w:gridCol w:w="541"/>
                            <w:gridCol w:w="359"/>
                            <w:gridCol w:w="1080"/>
                            <w:gridCol w:w="1080"/>
                            <w:gridCol w:w="1080"/>
                            <w:gridCol w:w="2474"/>
                        </w:tblGrid>
                        <w:tr w:rsidR="00C5116B" w:rsidRPr="0044668B" w14:paraId="2E88835E" w14:textId="77777777" w:rsidTr="0012006F">
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="8522" w:type="dxa"/>
                                    <w:gridSpan w:val="7"/>
                                </w:tcPr>
                                <w:p w14:paraId="36EC9E7A" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00340BC8">
                                    <w:pPr>
                                        <w:jc w:val="left"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>课程名称：</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${data.courseName!""}</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve">          </w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>教学班级：</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${data.educlassNames!''}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="00C5116B" w:rsidRPr="0044668B" w14:paraId="7D8A410F" w14:textId="77777777" w:rsidTr="0012006F">
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="8522" w:type="dxa"/>
                                    <w:gridSpan w:val="7"/>
                                </w:tcPr>
                                <w:p w14:paraId="462466D8" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="008A5382">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>课程考核合理性审核内容</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="00C5116B" w:rsidRPr="0044668B" w14:paraId="1DECA7F9" w14:textId="77777777" w:rsidTr="0012006F">
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1908" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="3BEEDF6D" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="0065527A">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>毕业要求指标点</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="900" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="55FE1F8E" w14:textId="77777777" w:rsidR="0065527A" w:rsidRDefault="00C5116B" w:rsidP="0065527A">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>试题</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w14:paraId="110D64F4" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="0065527A">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>难度</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1080" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="49E69A5D" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="0065527A">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>分值设置</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1080" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="2EBCC4B1" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="0065527A">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>知识能力点覆盖</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1080" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="1AD28B27" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="0065527A">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>考核形式</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2474" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="1A310A46" w14:textId="77777777" w:rsidR="00057987" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="0065527A">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>考核结果判定的</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w14:paraId="6663CE42" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="0065527A">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>严格程度</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#if data.ccCourseGradecomposeIndications??]
                        [#list data.ccCourseGradecomposeIndications as courseGradecomposeIndicationMap]
                            [#assign indicationInfo = {} gradecomposeNameList = [] graduateIndexNum = -1 indicationIndexNum = -1 indicationContent = "" ]
                            [#if courseGradecomposeIndicationMap.indicationId?? && indicationMap?? && indicationMap?size>0]
                                [#assign indicationInfo = indicationMap[courseGradecomposeIndicationMap.indicationId]]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.graduateIndexNum??]
                                [#assign graduateIndexNum = indicationInfo.graduateIndexNum]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.indicationIndexNum??]
                                [#assign indicationIndexNum = indicationInfo.indicationIndexNum]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.indicationContent??]
                                [#assign indicationContent = indicationInfo.indicationContent]
                            [/#if]
                            [#if courseGradecomposeIndicationMap.gradecomposeIndications??]
                                [#list courseGradecomposeIndicationMap.gradecomposeIndications as gradecompossIndication]
                                [#if gradecompossIndication.gradecomposeName??]
                                [#assign gradecomposeNameList = gradecomposeNameList + [gradecompossIndication.gradecomposeName]]
                                [/#if]
                                [/#list]
                            [/#if]
                        <w:tr w:rsidR="0065527A" w:rsidRPr="0044668B" w14:paraId="10EE80DE" w14:textId="77777777" w:rsidTr="0012006F">
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1908" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="1AF4C7D0" w14:textId="77777777" w:rsidR="0065527A" w:rsidRPr="00AD4E21" w:rsidRDefault="0065527A" w:rsidP="001B455F">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00AD4E21">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if graduateIndexNum?? && (graduateIndexNum >= 0)]${graduateIndexNum}[/#if][#if indicationIndexNum?? && (indicationIndexNum >= 0)].${indicationIndexNum}[/#if][#if indicationContent??]${indicationContent}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="900" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="3BA22D8F" w14:textId="77777777" w:rsidR="0065527A" w:rsidRPr="0044668B" w:rsidRDefault="0065527A" w:rsidP="004A1BC7">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t></w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1080" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="57BAAD3A" w14:textId="77777777" w:rsidR="0065527A" w:rsidRPr="0044668B" w:rsidRDefault="0065527A" w:rsidP="004A1BC7">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t></w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1080" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="2F946196" w14:textId="77777777" w:rsidR="0065527A" w:rsidRPr="0044668B" w:rsidRDefault="0065527A" w:rsidP="00A71236">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t></w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1080" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="6A709753" w14:textId="77777777" w:rsidR="0065527A" w:rsidRPr="00AD4E21" w:rsidRDefault="0065527A" w:rsidP="000E1EA9">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#list gradecomposeNameList][#items as gradecomposeName]${gradecomposeName}[#sep ]，[/#items][#else][/#list]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2474" w:type="dxa"/>
                                </w:tcPr>
                                <w:p w14:paraId="7C6A3134" w14:textId="77777777" w:rsidR="0065527A" w:rsidRPr="0044668B" w:rsidRDefault="0065527A" w:rsidP="004A1BC7">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="000E1EA9">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t></w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [/#list]
                        [/#if]
                        <w:tr w:rsidR="000E1EA9" w:rsidRPr="0044668B" w14:paraId="4969D18E" w14:textId="77777777" w:rsidTr="0012006F">
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="8522" w:type="dxa"/>
                                    <w:gridSpan w:val="7"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="7DEEB384" w14:textId="77777777" w:rsidR="000E1EA9" w:rsidRPr="0044668B" w:rsidRDefault="000E1EA9" w:rsidP="008A5382">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>课程考核合理性审核</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="000E1EA9" w:rsidRPr="0044668B" w14:paraId="6CD7901E" w14:textId="77777777" w:rsidTr="0012006F">
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2449" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="7B98FF23" w14:textId="77777777" w:rsidR="000E1EA9" w:rsidRPr="0044668B" w:rsidRDefault="000E1EA9" w:rsidP="008A5382">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>该课程考核是否合理</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="6073" w:type="dxa"/>
                                    <w:gridSpan w:val="5"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="3780D7A7" w14:textId="77777777" w:rsidR="000E1EA9" w:rsidRPr="0044668B" w:rsidRDefault="000E1EA9" w:rsidP="008A5382">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                                <w:p w14:paraId="64DDF7E9" w14:textId="77777777" w:rsidR="000E1EA9" w:rsidRPr="0044668B" w:rsidRDefault="000E1EA9" w:rsidP="008A5382">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                                <w:p w14:paraId="327C821B" w14:textId="77777777" w:rsidR="000E1EA9" w:rsidRPr="0044668B" w:rsidRDefault="000E1EA9" w:rsidP="008A5382">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                                <w:p w14:paraId="6D982531" w14:textId="77777777" w:rsidR="000E1EA9" w:rsidRPr="0044668B" w:rsidRDefault="000E1EA9" w:rsidP="00976486">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve">                            </w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>审核人：</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="00452DEA">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="FF0000"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>（</w:t>
                                    </w:r>
                                    <w:r w:rsidR="00976486" w:rsidRPr="00452DEA">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="FF0000"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>同</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="00452DEA">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="FF0000"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>试卷</w:t>
                                    </w:r>
                                    <w:r w:rsidR="00976486" w:rsidRPr="00452DEA">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="FF0000"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>审核人</w:t>
                                    </w:r>
                                    <w:r w:rsidR="00452DEA" w:rsidRPr="00452DEA">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="FF0000"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>或批准人</w:t>
                                    </w:r>
                                    <w:r w:rsidR="00976486" w:rsidRPr="00452DEA">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:color w:val="FF0000"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>）</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w14:paraId="00D7AB69" w14:textId="77777777" w:rsidR="000E1EA9" w:rsidRPr="0044668B" w:rsidRDefault="000E1EA9" w:rsidP="0044668B">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve"></w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve"></w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve">                             </w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>日期：</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve">     </w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>年</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve">     </w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>月</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve">     </w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>日</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                    </w:tbl>
                    <w:p w14:paraId="24C0B8DA" w14:textId="77777777" w:rsidR="00057987" w:rsidRDefault="00057987" w:rsidP="00C5116B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="10803C12" w14:textId="77777777" w:rsidR="00AD5994" w:rsidRPr="00010C4F" w:rsidRDefault="00057987" w:rsidP="00AD5994">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:br w:type="page"/>
                        </w:r>
                        <w:r w:rsidR="00AD5994">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>三</w:t>
                        </w:r>
                        <w:r w:rsidR="00AD5994" w:rsidRPr="00010C4F">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>、</w:t>
                        </w:r>
                        <w:r w:rsidR="00AD5994" w:rsidRPr="00010C4F">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>评价方法</w:t>
                        </w:r>
                    </w:p>
                    [#if data.gradecomposes??]
                    [#assign tableCount = 2]
                    [#list data.gradecomposes as gradecomposeMap]
                    [#if gradecomposeMap.gradecomposeId??]
                    [#assign scoreSectionAndRemarkList = [] studentScoreList = [] courseGradecomposeIndicationList = [] courseGradecomposeIndicationIdList = [] existFirst = 0 existSecond = 0 ]
                    [#if data.scoreSectionAndRemarkMap?? && data.scoreSectionAndRemarkMap[gradecomposeMap.gradecomposeId]??]
                        [#assign scoreSectionAndRemarkList = data.scoreSectionAndRemarkMap[gradecomposeMap.gradecomposeId]]
                    [/#if]
                    [#if data.studentScoreMap?? && data.studentScoreMap[gradecomposeMap.gradecomposeId]??]
                        [#assign studentScoreList = data.studentScoreMap[gradecomposeMap.gradecomposeId]]
                    [/#if]
                    [#if courseGradecomposeIndicationMapOrigin?? && courseGradecomposeIndicationMapOrigin[gradecomposeMap.gradecomposeId]??]
                        [#assign courseGradecomposeIndicationList = courseGradecomposeIndicationMapOrigin[gradecomposeMap.gradecomposeId]]
                    [/#if]
                    <w:p w14:paraId="488AED0B" w14:textId="77777777" w:rsidR="00AD5994" w:rsidRPr="0044668B" w:rsidRDefault="00057987" w:rsidP="00AD5994">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        [#if !(gradecomposeMap?is_first)]
                        <w:r>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:br w:type="page"/>
                        </w:r>
                        [/#if]
                        <w:r w:rsidR="00AD5994" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>${gradecomposeMap?counter}、${gradecomposeMap.gradecomposeName!""}</w:t>
                        </w:r>
                        <w:r w:rsidR="00AD5994" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>成绩课程考核评分表分析法</w:t>
                        </w:r>
                    </w:p>
                    [#if scoreSectionAndRemarkList?? && scoreSectionAndRemarkList?size>0]
                    [#assign tableCount+=1  existFirst=1]
                    <w:p w14:paraId="14B490AC" w14:textId="77777777" w:rsidR="00AD5994" w:rsidRPr="003D6DD9" w:rsidRDefault="003D6DD9" w:rsidP="00AD5994">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t xml:space="preserve">    </w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>根据表</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>1</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>，</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>${gradecomposeMap.gradecomposeName!""}</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>各项指标点的考核评分如表</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>${tableCount!""}</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>所示。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="6E1EC982" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C5116B">
                        <w:pPr>
                            <w:spacing w:line="600" w:lineRule="exact"/>
                            <w:ind w:firstLine="420"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>表</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve">${tableCount!""}  </w:t>
                        </w:r>
                        <w:r w:rsidR="001E6076" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>《</w:t>
                        </w:r>
                        <w:r w:rsidR="007E65EC">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.courseName!""}</w:t>
                        </w:r>
                        <w:r w:rsidR="001E6076" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>》${gradecomposeMap.gradecomposeName!""}</w:t>
                        </w:r>
                        <w:r w:rsidR="008C7506">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>按</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>毕业要求指标点考核评分表</w:t>
                        </w:r>
                    </w:p>
                    [#assign maxSizeSectionAndRemark = 0]
                    [#list scoreSectionAndRemarkList as scoreSectionAndRemark]
                        [#if scoreSectionAndRemark?size>0]
                            [#list scoreSectionAndRemark?keys as key]
                                [#if scoreSectionAndRemark[key]?? && scoreSectionAndRemark[key]?size>0 && (scoreSectionAndRemark[key]?size>maxSizeSectionAndRemark)]
                                    [#assign maxSizeSectionAndRemark = scoreSectionAndRemark[key]?size]
                                [/#if]
                            [/#list]
                        [/#if]
                    [/#list]
                    <w:tbl>
                        <w:tblPr>
                            <w:tblW w:w="8243" w:type="dxa"/>
                            <w:jc w:val="center"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:left w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:bottom w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:right w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:insideH w:val="single" w:sz="6" w:space="0" w:color="auto"/>
                                <w:insideV w:val="single" w:sz="6" w:space="0" w:color="auto"/>
                            </w:tblBorders>
                            <w:tblLook w:val="01E0" w:firstRow="1" w:lastRow="1" w:firstColumn="1" w:lastColumn="1" w:noHBand="0" w:noVBand="0"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="1543"/>
                            [#if maxSizeSectionAndRemark>0]
                            [#assign colWidth = 6700/maxSizeSectionAndRemark]
                            [#list 1..maxSizeSectionAndRemark as i]
                            <w:gridCol w:w="${colWidth?c}"/>
                            [/#list]
                            [/#if]
                        </w:tblGrid>
                        <w:tr w:rsidR="00C5116B" w:rsidRPr="0044668B" w14:paraId="6DFF41AD" w14:textId="77777777">
                            <w:trPr>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="8243" w:type="dxa"/>
                                    <w:gridSpan w:val="${(maxSizeSectionAndRemark+1)?c}"/>
                                </w:tcPr>
                                <w:p w14:paraId="2E2574D1" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="002017FC">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>课程名称：</w:t>
                                    </w:r>
                                    <w:r w:rsidR="002017FC" w:rsidRPr="002017FC">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${data.courseName!""}</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="002017FC">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve"></w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve"></w:t>
                                    </w:r>
                                    <w:r w:rsidR="005752C4" w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t xml:space="preserve">          </w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>教学班级：</w:t>
                                    </w:r>
                                    [#if data.educlassNames??]
                                    <w:r w:rsidR="002017FC" w:rsidRPr="002017FC">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${data.educlassNames!''}</w:t>
                                    </w:r>
                                    [/#if]
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#list scoreSectionAndRemarkList as scoreSectionAndRemark]
                        <w:tr w:rsidR="001E6076" w:rsidRPr="0044668B" w14:paraId="28BBB408" w14:textId="77777777">
                            <w:trPr>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1543" w:type="dxa"/>
                                </w:tcPr>
                                <w:p w14:paraId="74877C2F" w14:textId="77777777" w:rsidR="001E6076" w:rsidRPr="0044668B" w:rsidRDefault="001E6076" w:rsidP="0044668B">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:ind w:firstLineChars="147" w:firstLine="309"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>指标点</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [#if scoreSectionAndRemark?size>0]
                            [#list scoreSectionAndRemark?keys as key]
                            [#if scoreSectionAndRemark[key]?? && scoreSectionAndRemark[key]?size>0]
                            [#assign width = (6700/(scoreSectionAndRemark[key]?size))?int]
                            [#list scoreSectionAndRemark[key] as scoreSection]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="${width?c}" w:type="auto"/>
                                </w:tcPr>
                                <w:p w14:paraId="0CB41E79" w14:textId="77777777" w:rsidR="001E6076" w:rsidRPr="0044668B" w:rsidRDefault="001E6076" w:rsidP="00761230">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${scoreSection.scoreSection!""}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [/#list]
                            [#if maxSizeSectionAndRemark>scoreSectionAndRemark[key]?size]
                            [#assign length = maxSizeSectionAndRemark - scoreSectionAndRemark[key]?size]
                            [#list 1..length as i]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="${width?c}" w:type="auto"/>
                                </w:tcPr>
                                <w:p w14:paraId="0CB41E79" w14:textId="77777777" w:rsidR="001E6076" w:rsidRPr="0044668B" w:rsidRDefault="001E6076" w:rsidP="00761230">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t></w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [/#list]
                            [/#if]
                            [/#if]
                            [/#list]
                            [/#if]
                        </w:tr>
                        <w:tr w:rsidR="001E6076" w:rsidRPr="0044668B" w14:paraId="6D5802FE" w14:textId="77777777">
                            <w:trPr>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            [#if scoreSectionAndRemark?size>0]
                            [#list scoreSectionAndRemark?keys as key]
                                [#assign indicationId = "" indicationInfo = {} graduateIndexNum = -1 indicationIndexNum = -1 indicationContent = ""]
                                [#if gradecomposeAndIndicationMap??]
                                    [#assign indicationId = gradecomposeAndIndicationMap[key]]
                                [/#if]
                                [#if indicationId?? && indicationMap?? && indicationMap?size>0]
                                    [#assign indicationInfo = indicationMap[indicationId]]
                                [/#if]
                                [#if indicationInfo?? && indicationInfo.graduateIndexNum??]
                                    [#assign graduateIndexNum = indicationInfo.graduateIndexNum]
                                [/#if]
                                [#if indicationInfo?? && indicationInfo.indicationIndexNum??]
                                    [#assign indicationIndexNum = indicationInfo.indicationIndexNum]
                                [/#if]
                                [#if indicationInfo?? && indicationInfo.indicationContent??]
                                    [#assign indicationContent = indicationInfo.indicationContent]
                                [/#if]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1543" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="43F6680E" w14:textId="77777777" w:rsidR="00651D94" w:rsidRDefault="007E65EC" w:rsidP="00651D94">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if graduateIndexNum?? && (graduateIndexNum >=0) ]${graduateIndexNum}[/#if][#if indicationIndexNum?? && (indicationIndexNum >0) ].${indicationIndexNum}[/#if]</w:t>
                                    </w:r>
                                    <w:r w:rsidR="00651D94" w:rsidRPr="00651D94">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if indicationContent??]${indicationContent}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w14:paraId="5F02B00F" w14:textId="77777777" w:rsidR="001E6076" w:rsidRPr="0044668B" w:rsidRDefault="001E6076" w:rsidP="007E65EC">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            [#if scoreSectionAndRemark[key]?? && scoreSectionAndRemark[key]?size>0]
                            [#assign width = (6700/(scoreSectionAndRemark[key]?size))?int]
                            [#list scoreSectionAndRemark[key] as remark]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="${width?c}" w:type="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="05291A87" w14:textId="77777777" w:rsidR="003D6DD9" w:rsidRDefault="00651D94" w:rsidP="004A1BC7">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00651D94">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${remark.scoreRemark!""}</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w14:paraId="14EB4FD0" w14:textId="77777777" w:rsidR="003D6DD9" w:rsidRPr="0044668B" w:rsidRDefault="003D6DD9" w:rsidP="004A1BC7">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            [/#list]
                            [#if maxSizeSectionAndRemark>scoreSectionAndRemark[key]?size]
                            [#assign length = maxSizeSectionAndRemark - scoreSectionAndRemark[key]?size]
                            [#list 1..length as i]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="${width?c}" w:type="auto"/>
                                </w:tcPr>
                                <w:p w14:paraId="0CB41E79" w14:textId="77777777" w:rsidR="001E6076" w:rsidRPr="0044668B" w:rsidRDefault="001E6076" w:rsidP="00761230">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t></w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [/#list]
                            [/#if]
                            [/#if]
                            [/#list]
                            [/#if]
                        </w:tr>
                        [/#list]
                    </w:tbl>
                    <w:p w14:paraId="12C836CA" w14:textId="77777777" w:rsidR="00761230" w:rsidRPr="0044668B" w:rsidRDefault="00761230" w:rsidP="00761230">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0083629A">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>说明：指标点参照培养计划及课程达成度权重系数表，各指标点效果分值按大纲要求分配，所有分值总和为平时表现分数。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="25EAA266" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C5116B">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="3564F3AE" w14:textId="77777777" w:rsidR="00803BCB" w:rsidRPr="0044668B" w:rsidRDefault="00803BCB" w:rsidP="00C5116B">
                        <w:pPr>
                            <w:ind w:firstLine="420"/>
                            <w:rPr>
                                <w:rFonts w:ascii="黑体" w:eastAsia="黑体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="7CE51EC4" w14:textId="77777777" w:rsidR="00057987" w:rsidRPr="00203C9E" w:rsidRDefault="00057987" w:rsidP="00C5116B">
                        <w:pPr>
                            <w:ind w:firstLine="420"/>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    [/#if]
                    [#if studentScoreList?? && studentScoreList?size>0]
                    [#assign tableCount+=1  existSecond=1]
                    <w:p w14:paraId="7CDA4D1F" w14:textId="77777777" w:rsidR="00AD5994" w:rsidRPr="003D6DD9" w:rsidRDefault="00057987" w:rsidP="003D6DD9">
                        <w:pPr>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:ind w:firstLineChars="200" w:firstLine="360"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        [#if existFirst==1]
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:br w:type="page"/>
                        </w:r>
                        [/#if]
                        <w:r w:rsidR="00AD5994" w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:lastRenderedPageBreak/>
                            <w:t>依据评分表评价成绩</w:t>
                        </w:r>
                        <w:r w:rsidR="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>，如表</w:t>
                        </w:r>
                        <w:r w:rsidR="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>${tableCount!""}</w:t>
                        </w:r>
                        <w:r w:rsidR="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>所示。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="5DCD8B48" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C5116B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>表</w:t>
                        </w:r>
                        <w:r w:rsidR="00803BCB" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${tableCount!""}</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> </w:t>
                        </w:r>
                        <w:r w:rsidR="007E65EC">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>[#if data.startYear??]${data.startYear?c}[/#if]-[#if data.endYear??]${data.endYear?c}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidR="00803BCB" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>学年《</w:t>
                        </w:r>
                        <w:r w:rsidR="007E65EC">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.courseName!""}</w:t>
                        </w:r>
                        <w:r w:rsidR="00803BCB" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>》</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>课程</w:t>
                        </w:r>
                        <w:r w:rsidR="00803BCB" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${gradecomposeMap.gradecomposeName!""}</w:t>
                        </w:r>
                        <w:r w:rsidR="008C7506">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>按</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>毕业要求指标点考核成绩统计表</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblW w:w="6846" w:type="dxa"/>
                            <w:jc w:val="center"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:left w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:bottom w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:right w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:insideH w:val="single" w:sz="6" w:space="0" w:color="auto"/>
                                <w:insideV w:val="single" w:sz="6" w:space="0" w:color="auto"/>
                            </w:tblBorders>
                            <w:tblLook w:val="0000" w:firstRow="0" w:lastRow="0" w:firstColumn="0" w:lastColumn="0" w:noHBand="0" w:noVBand="0"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="774"/>
                            <w:gridCol w:w="1263"/>
                            <w:gridCol w:w="919"/>
                            <w:gridCol w:w="897"/>
                            <w:gridCol w:w="1002"/>
                            <w:gridCol w:w="936"/>
                            <w:gridCol w:w="1055"/>
                        </w:tblGrid>
                        <w:tr w:rsidR="007E65EC" w:rsidRPr="0044668B" w14:paraId="5898B5F1" w14:textId="77777777">
                            <w:trPr>
                                <w:trHeight w:val="170"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="774" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="31652E16" w14:textId="77777777" w:rsidR="007E65EC" w:rsidRPr="0044668B" w:rsidRDefault="007E65EC" w:rsidP="00803BCB">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>序号</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1263" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="11A5CF2C" w14:textId="77777777" w:rsidR="007E65EC" w:rsidRPr="0044668B" w:rsidRDefault="007E65EC" w:rsidP="00803BCB">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>学号</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="919" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="210981E9" w14:textId="77777777" w:rsidR="007E65EC" w:rsidRPr="0044668B" w:rsidRDefault="007E65EC" w:rsidP="00803BCB">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>姓名</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="897" w:type="dxa"/>
                                </w:tcPr>
                                <w:p w14:paraId="04C98098" w14:textId="77777777" w:rsidR="007E65EC" w:rsidRPr="0044668B" w:rsidRDefault="007E65EC" w:rsidP="007E65EC">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:spacing w:beforeLines="50" w:before="156"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>班级</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [#if courseGradecomposeIndicationList??]
                            [#list courseGradecomposeIndicationList as courseGradecomposeIndicationInfo]
                            [#assign indicationInfo = {} graduateIndexNum = -1 indicationIndexNum = -1]
                            [#assign courseGradecomposeIndicationIdList = courseGradecomposeIndicationIdList + [courseGradecomposeIndicationInfo.gradecomposeIndicationId]]
                            [#if courseGradecomposeIndicationInfo.indicationId?? && indicationMap?? && indicationMap?size>0]
                            [#assign indicationInfo = indicationMap[courseGradecomposeIndicationInfo.indicationId]]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.graduateIndexNum??]
                                [#assign graduateIndexNum = indicationInfo.graduateIndexNum]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.indicationIndexNum??]
                                [#assign indicationIndexNum = indicationInfo.indicationIndexNum]
                            [/#if]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1002" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="4156FC7E" w14:textId="77777777" w:rsidR="007E65EC" w:rsidRPr="0044668B" w:rsidRDefault="007E65EC" w:rsidP="00803BCB">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>指标点</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if graduateIndexNum?? && (graduateIndexNum >=0) ]${graduateIndexNum}[/#if][#if indicationIndexNum?? && (indicationIndexNum >0) ].${indicationIndexNum}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [/#list]
                            [/#if]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1055" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="359CBA02" w14:textId="77777777" w:rsidR="007E65EC" w:rsidRPr="0044668B" w:rsidRDefault="007E65EC" w:rsidP="00803BCB">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>总成绩</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#assign totalStudentScore=0 ]
                        [#list studentScoreList as studentScore]
                        <w:tr w:rsidR="00CA0621" w:rsidRPr="0044668B" w14:paraId="6599B465" w14:textId="77777777">
                            <w:trPr>
                                <w:trHeight w:val="170"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="774" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="7588C8DF" w14:textId="77777777" w:rsidR="00CA0621" w:rsidRPr="0044668B" w:rsidRDefault="00CA0621" w:rsidP="00803BCB">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${studentScore?counter}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1263" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="bottom"/>
                                </w:tcPr>
                                <w:p w14:paraId="1B8F098D" w14:textId="77777777" w:rsidR="00CA0621" w:rsidRDefault="00CA0621" w:rsidP="0066627A">
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${studentScore.studentNo}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="919" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="bottom"/>
                                </w:tcPr>
                                <w:p w14:paraId="596E277C" w14:textId="77777777" w:rsidR="00CA0621" w:rsidRDefault="00CA0621" w:rsidP="0066627A">
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${studentScore.studentName}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="897" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="71EDA1E7" w14:textId="77777777" w:rsidR="00CA0621" w:rsidRPr="00761230" w:rsidRDefault="00CA0621" w:rsidP="0066627A">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="left"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00761230">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${studentScore.className}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [#if courseGradecomposeIndicationIdList??]
                            [#list courseGradecomposeIndicationIdList as courseGradecomposeIndicationId]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1002" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="4468FD4D" w14:textId="77777777" w:rsidR="00CA0621" w:rsidRPr="0044668B" w:rsidRDefault="00976486" w:rsidP="0066627A">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${(studentScore[courseGradecomposeIndicationId])!""}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [#if (studentScore[courseGradecomposeIndicationId]??) && studentScore[courseGradecomposeIndicationId]?is_number]
                            [#assign totalStudentScore = totalStudentScore+studentScore[courseGradecomposeIndicationId]]
                            [/#if]
                            [/#list]
                            [/#if]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1055" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="0B0BFA9B" w14:textId="77777777" w:rsidR="00CA0621" w:rsidRPr="0044668B" w:rsidRDefault="000D51C8" w:rsidP="0066627A">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${studentScore.totalScore!""}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [/#list]
                        <w:tr w:rsidR="00CA0621" w:rsidRPr="0044668B" w14:paraId="0AC7980E" w14:textId="77777777">
                            <w:trPr>
                                <w:trHeight w:val="170"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3853" w:type="dxa"/>
                                    <w:gridSpan w:val="4"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="69A3AA95" w14:textId="77777777" w:rsidR="00CA0621" w:rsidRPr="0044668B" w:rsidRDefault="00CA0621" w:rsidP="00803BCB">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>平均成绩</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [#if courseGradecomposeIndicationIdList??]
                            [#list courseGradecomposeIndicationIdList as courseGradecomposeIndicationId]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1002" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="46D40074" w14:textId="77777777" w:rsidR="00CA0621" w:rsidRPr="0044668B" w:rsidRDefault="000D51C8" w:rsidP="00803BCB">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if data.averageScoreMap??]${(data.averageScoreMap[courseGradecomposeIndicationId])!""}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            [/#list]
                            [/#if]
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1055" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="10F10E8B" w14:textId="77777777" w:rsidR="00CA0621" w:rsidRPr="0044668B" w:rsidRDefault="000D51C8" w:rsidP="00803BCB">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                            <w:color w:val="000000"/>
                                            <w:kern w:val="0"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if totalStudentScore?? && studentScoreList?? && studentScoreList?size>0]${totalStudentScore/(studentScoreList?size)}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                    </w:tbl>
                    <w:p w14:paraId="26211559" w14:textId="77777777" w:rsidR="00057987" w:rsidRDefault="007774B8" w:rsidP="007774B8">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t xml:space="preserve">    </w:t>
                        </w:r>
                        <w:r w:rsidRPr="00615618">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>说明：</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>毕业要求</w:t>
                        </w:r>
                        <w:r w:rsidRPr="004F4036">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>指标点</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>参照培养计划及</w:t>
                        </w:r>
                        <w:r w:rsidRPr="004F4036">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>课程达成度权重系数表</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>。</w:t>
                        </w:r>
                    </w:p>
                    [/#if]
                    [/#if]
                    [/#list]
                    [/#if]
                    <w:p w14:paraId="31589CD3" w14:textId="77777777" w:rsidR="0044668B" w:rsidRDefault="000C69A5" w:rsidP="003D6DD9">
                        <w:pPr>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidR="00057987">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:br w:type="page"/>
                        </w:r>
                        <w:r w:rsidR="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:lastRenderedPageBreak/>
                            <w:t>四</w:t>
                        </w:r>
                        <w:r w:rsidR="003D6DD9" w:rsidRPr="00010C4F">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>、</w:t>
                        </w:r>
                        <w:r w:rsidR="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>对课程目标达成度进行评价</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="2F851F79" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C5116B">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>表</w:t>
                        </w:r>
                        <w:bookmarkStart w:id="3" w:name="OLE_LINK1"/>
                        <w:bookmarkStart w:id="4" w:name="OLE_LINK2"/>
                        <w:r w:rsidR="001C204E">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>[#if tableCount??]${tableCount+1}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidR="00057987" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> </w:t>
                        </w:r>
                        <w:r w:rsidR="007E65EC">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>[#if data.startYear??]${data.startYear?c}[/#if]-[#if data.endYear??]${data.endYear?c}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidR="0080713A" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>学年《</w:t>
                        </w:r>
                        <w:r w:rsidR="007E65EC">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.courseName!""}</w:t>
                        </w:r>
                        <w:r w:rsidR="0080713A" w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>》</w:t>
                        </w:r>
                        <w:bookmarkEnd w:id="3"/>
                        <w:bookmarkEnd w:id="4"/>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>课程目标达成度评价</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblW w:w="9531" w:type="dxa"/>
                            <w:jc w:val="center"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:left w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:bottom w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:right w:val="single" w:sz="12" w:space="0" w:color="auto"/>
                                <w:insideH w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:insideV w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            </w:tblBorders>
                            <w:tblLook w:val="01E0" w:firstRow="1" w:lastRow="1" w:firstColumn="1" w:lastColumn="1" w:noHBand="0" w:noVBand="0"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="757"/>
                            <w:gridCol w:w="2048"/>
                            <w:gridCol w:w="2072"/>
                            <w:gridCol w:w="1548"/>
                            <w:gridCol w:w="1300"/>
                            <w:gridCol w:w="996"/>
                            <w:gridCol w:w="810"/>
                        </w:tblGrid>
                        <w:tr w:rsidR="000D51C8" w:rsidRPr="0044668B" w14:paraId="67BC4620" w14:textId="77777777">
                            <w:trPr>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2805" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="2115301D" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>课程支撑的毕业要求</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2072" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="44E38321" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>考核内容</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w14:paraId="672A6479" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>（试卷/平时表现</w:t>
                                    </w:r>
                                    <w:r w:rsidR="001C204E">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>/实验</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1548" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="605D6DD7" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>目标分值</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1300" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="7B4BC2EB" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>平均成绩</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="996" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="2481AE34" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>课程目标</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w14:paraId="1E04CC0C" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>达成度</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="810" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p w14:paraId="3914A2F4" w14:textId="77777777" w:rsidR="00C5116B" w:rsidRPr="0044668B" w:rsidRDefault="00C5116B" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="left"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>记录文档</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#assign conclusionList = []]
                        [#if data.ccCourseGradecomposeIndications??]
                        [#list data.ccCourseGradecomposeIndications as courseGradecomposeIndicationMap]
                            [#assign indicationInfo = {} gradecomposeNameList = [] graduateIndexNum = -1 indicationIndexNum = -1 indicationContent = "" averageGradetotal = 0 targetGradetotal = 0 achivement = 0 ]
                            [#if courseGradecomposeIndicationMap.indicationId?? && indicationMap?? && indicationMap?size>0]
                                [#assign indicationInfo = indicationMap[courseGradecomposeIndicationMap.indicationId]]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.graduateIndexNum??]
                                [#assign graduateIndexNum = indicationInfo.graduateIndexNum]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.indicationIndexNum??]
                                [#assign indicationIndexNum = indicationInfo.indicationIndexNum]
                            [/#if]
                            [#if indicationInfo?? && indicationInfo.indicationContent??]
                                [#assign indicationContent = indicationInfo.indicationContent]
                            [/#if]
                        [#if courseGradecomposeIndicationMap.gradecomposeIndications?? && courseGradecomposeIndicationMap.gradecomposeIndications?size > 0]
                        [#assign averageGradetotalTemp = 0 targetGradetotalTemp = 0]
                        [#list courseGradecomposeIndicationMap.gradecomposeIndications as gradecompossIndication]
                        [#if gradecompossIndication.afterCalculateMaxScore??]
                            [#assign targetGradetotalTemp = targetGradetotalTemp+gradecompossIndication.afterCalculateMaxScore]
                        [/#if]
                        [#if gradecompossIndication.afterCalculateAverageScore??]
                            [#assign averageGradetotalTemp = averageGradetotalTemp+gradecompossIndication.afterCalculateAverageScore]
                        [/#if]
                        [/#list]
                        [#list courseGradecomposeIndicationMap.gradecomposeIndications as gradecompossIndication]
                        [#if gradecompossIndication?is_first]
                        [#assign conclusionList = conclusionList + [{"graduateIndexNum":graduateIndexNum, "indicationIndexNum":indicationIndexNum, "indicationContent":indicationContent, "targetGradetotalTemp":targetGradetotalTemp, "averageGradetotalTemp":averageGradetotalTemp}]]
                        <w:tr w:rsidR="00E408C7" w:rsidRPr="0044668B" w14:paraId="74D21DFE" w14:textId="77777777" w:rsidTr="00EA1D7C">
                            <w:trPr>
                                <w:trHeight w:val="376"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="757" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="6601320A" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="0044668B" w:rsidRDefault="00E408C7" w:rsidP="00057987">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>毕业要求</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if graduateIndexNum?? && (graduateIndexNum>=0)]${graduateIndexNum}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2048" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="56E9A4F7" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="0044668B" w:rsidRDefault="00E408C7" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00483201">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if graduateIndexNum?? && (graduateIndexNum>=0)]${graduateIndexNum}[/#if][#if indicationIndexNum?? && (indicationIndexNum >= 0)].${indicationIndexNum}[/#if][#if indicationContent??]${indicationContent}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2072" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="5C6A70D2" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="00AD4E21" w:rsidRDefault="00E408C7" w:rsidP="00E408C7">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.gradecomposeName??]${gradecompossIndication.gradecomposeName}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1543" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="7A770100" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="00813F15" w:rsidRDefault="00E408C7" w:rsidP="00E408C7">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00813F15">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.maxScore??]${gradecompossIndication.maxScore}[/#if]*[#if gradecompossIndication.weight??]${gradecompossIndication.weight}[/#if]（[#if gradecompossIndication.maxScore?? && gradecompossIndication.weight??]${gradecompossIndication.maxScore*gradecompossIndication.weight}[#assign targetGradetotal=(targetGradetotal+(gradecompossIndication.maxScore*gradecompossIndication.weight))][/#if]分）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1305" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="426E5F9A" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="00813F15" w:rsidRDefault="00E408C7" w:rsidP="00E408C7">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.averageScore??]${gradecompossIndication.averageScore}[/#if]*[#if gradecompossIndication.weight??]${gradecompossIndication.weight}[/#if]（[#if gradecompossIndication.averageScore?? && gradecompossIndication.weight??]${gradecompossIndication.averageScore*gradecompossIndication.weight}[#assign averageGradetotal=(averageGradetotal+(gradecompossIndication.averageScore*gradecompossIndication.weight))][/#if]分）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="996" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="1F566180" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="0044668B" w:rsidRDefault="00E408C7" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if targetGradetotalTemp?? && averageGradetotalTemp?? && targetGradetotalTemp>0]${averageGradetotalTemp/targetGradetotalTemp}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="810" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p w14:paraId="4FE73B13" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="000D51C8" w:rsidRDefault="00E408C7" w:rsidP="000D51C8">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="left"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#else]
                        <w:tr w:rsidR="00E408C7" w:rsidRPr="0044668B" w14:paraId="2D936B07" w14:textId="77777777" w:rsidTr="00EA1D7C">
                            <w:trPr>
                                <w:trHeight w:val="373"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="757" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="3BD58E2A" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="0044668B" w:rsidRDefault="00E408C7" w:rsidP="00057987">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2048" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="0ABBA286" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="00483201" w:rsidRDefault="00E408C7" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2072" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="5B56AA2C" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRDefault="00E408C7" w:rsidP="0066627A">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00AD4E21">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.gradecomposeName??]${gradecompossIndication.gradecomposeName}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1543" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="26CB3C5E" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="00813F15" w:rsidRDefault="00E408C7" w:rsidP="00E408C7">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00813F15">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.maxScore??]${gradecompossIndication.maxScore}[/#if]*[#if gradecompossIndication.weight??]${gradecompossIndication.weight}[/#if]（[#if gradecompossIndication.maxScore?? && gradecompossIndication.weight??]${gradecompossIndication.maxScore*gradecompossIndication.weight}[#assign targetGradetotal=(targetGradetotal+(gradecompossIndication.maxScore*gradecompossIndication.weight))][/#if]分）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1305" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="5DD7A894" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRDefault="00E408C7" w:rsidP="00E408C7">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00813F15">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if gradecompossIndication.averageScore??]${gradecompossIndication.averageScore}[/#if]*[#if gradecompossIndication.weight??]${gradecompossIndication.weight}[/#if]（[#if gradecompossIndication.averageScore?? && gradecompossIndication.weight??]${gradecompossIndication.averageScore*gradecompossIndication.weight}[#assign averageGradetotal=(averageGradetotal+(gradecompossIndication.averageScore*gradecompossIndication.weight))][/#if]分）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="996" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="356B2C14" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRDefault="00E408C7" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="810" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p w14:paraId="6B86CDD6" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="000D51C8" w:rsidRDefault="00E408C7" w:rsidP="000D51C8">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="left"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [/#if]
                        [/#list]
                        <w:tr w:rsidR="00E408C7" w:rsidRPr="0044668B" w14:paraId="2D936B07" w14:textId="77777777" w:rsidTr="00EA1D7C">
                            <w:trPr>
                                <w:trHeight w:val="373"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="757" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="3BD58E2A" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="0044668B" w:rsidRDefault="00E408C7" w:rsidP="00057987">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2048" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="0ABBA286" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="00483201" w:rsidRDefault="00E408C7" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2072" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="5B56AA2C" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRDefault="00E408C7" w:rsidP="0066627A">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00AD4E21">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>合计</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1543" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="26CB3C5E" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="00813F15" w:rsidRDefault="00E408C7" w:rsidP="00E408C7">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00813F15">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${targetGradetotal!""}分</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1305" w:type="dxa"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="5DD7A894" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRDefault="00E408C7" w:rsidP="00E408C7">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00813F15">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${averageGradetotal!""}分</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="996" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="356B2C14" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRDefault="00E408C7" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="810" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                </w:tcPr>
                                <w:p w14:paraId="6B86CDD6" w14:textId="77777777" w:rsidR="00E408C7" w:rsidRPr="000D51C8" w:rsidRDefault="00E408C7" w:rsidP="000D51C8">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="left"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [/#if]
                        [/#list]
                        [/#if]
                        <w:tr w:rsidR="000D51C8" w:rsidRPr="0044668B" w14:paraId="462082BD" w14:textId="77777777">
                            <w:trPr>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="9531" w:type="dxa"/>
                                    <w:gridSpan w:val="7"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="733EAA84" w14:textId="77777777" w:rsidR="000D51C8" w:rsidRPr="0044668B" w:rsidRDefault="000D51C8" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>本课程毕业要求各项指标点达成度目标值为：</w:t>
                                    </w:r>
                                    <w:r w:rsidR="0065527A">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${data.targetValue!""}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="000D51C8" w:rsidRPr="0044668B" w14:paraId="21FD9B1B" w14:textId="77777777">
                            <w:trPr>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="9531" w:type="dxa"/>
                                    <w:gridSpan w:val="7"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="68445F01" w14:textId="77777777" w:rsidR="000D51C8" w:rsidRPr="0044668B" w:rsidRDefault="000D51C8" w:rsidP="00C307B8">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>课程的持续改进</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="000D51C8" w:rsidRPr="0044668B" w14:paraId="3E955C4C" w14:textId="77777777">
                            <w:trPr>
                                <w:trHeight w:val="627"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2805" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="19342A4F" w14:textId="77777777" w:rsidR="000D51C8" w:rsidRPr="0044668B" w:rsidRDefault="000D51C8" w:rsidP="00057987">
                                    <w:pPr>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="0044668B">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>评价结果系统地运用到课程教学的持续改进中</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="6726" w:type="dxa"/>
                                    <w:gridSpan w:val="5"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="0147E7D4" w14:textId="77777777" w:rsidR="000D51C8" w:rsidRPr="007210ED" w:rsidRDefault="000D51C8" w:rsidP="000D51C8">
                                    <w:pPr>
                                        <w:widowControl/>
                                        <w:snapToGrid w:val="0"/>
                                        <w:jc w:val="left"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                        </w:tr>
                    </w:tbl>
                    <w:p w14:paraId="0302B6DE" w14:textId="77777777" w:rsidR="00B30DBB" w:rsidRDefault="003D6DD9" w:rsidP="003D6DD9">
                        <w:pPr>
                            <w:rPr>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>说明：本课程毕业要求各项指标点达成度目标值的确定，按照《浙江科技学院关于印发学士学位授予办法的通知》浙科院教〔</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>2012</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>〕</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>5</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>号，在校学习期间学生平均学分绩点（毕业设计、毕业论文除外）达到</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>1.8</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>以上既可授予学位，而平均学分绩点</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>1.8</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>对应百分制为</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>68</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>分，五级制为中等。本课程计分制</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>为</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>百分制（</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>）；五级分制（</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>）</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>，</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>因此本课程毕业要求各项指标点课程目标达成度的目标值取</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>68</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>分</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>（</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>）</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>；</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>中等</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>（</w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidR="00A51DF9" w:rsidRPr="00A51DF9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>）</w:t>
                        </w:r>
                        <w:r w:rsidRPr="003D6DD9">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:t>。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="1A2E06C4" w14:textId="77777777" w:rsidR="00B30DBB" w:rsidRDefault="00B30DBB" w:rsidP="00B30DBB">
                        <w:pPr>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:sz w:val="18"/>
                                <w:szCs w:val="18"/>
                            </w:rPr>
                            <w:br w:type="page"/>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:lastRenderedPageBreak/>
                            <w:t>五</w:t>
                        </w:r>
                        <w:r w:rsidRPr="00010C4F">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>、</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>结论</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="72C655CF" w14:textId="77777777" w:rsidR="006B1664" w:rsidRPr="006B1664" w:rsidRDefault="006B1664" w:rsidP="006B1664">
                        <w:pPr>
                            <w:ind w:firstLineChars="200" w:firstLine="480"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="006B1664">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>根据表</w:t>
                        </w:r>
                        <w:r w:rsidR="001C204E">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>[#if tableCount??]${tableCount+1}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidRPr="006B1664">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>的本门课程各项指标点课程目标达成度的值，得到如表[#if tableCount??]${tableCount+2}[/#if]所示的课程目标达成度评价统计表。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w14:paraId="17880151" w14:textId="77777777" w:rsidR="006B1664" w:rsidRPr="006B1664" w:rsidRDefault="002462A8" w:rsidP="002462A8">
                        <w:pPr>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>表</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>[#if tableCount??]${tableCount+2}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"> </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>[#if data.startYear??]${data.startYear?c}[/#if]-[#if data.endYear??]${data.endYear?c}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>学年《</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.courseName!""}</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>》</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0044668B">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>课程目标达成度评价</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:b/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>统计表</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblW w:w="0" w:type="auto"/>
                            <w:jc w:val="center"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:insideH w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:insideV w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            </w:tblBorders>
                            <w:tblLook w:val="04A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="1"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="4261"/>
                            <w:gridCol w:w="4261"/>
                        </w:tblGrid>
                        <w:tr w:rsidR="002462A8" w:rsidRPr="00D33B61" w14:paraId="10A846C5" w14:textId="77777777" w:rsidTr="00D33B61">
                            <w:trPr>
                                <w:trHeight w:val="409"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="4261" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="57A73BC5" w14:textId="77777777" w:rsidR="002462A8" w:rsidRPr="00D33B61" w:rsidRDefault="002462A8" w:rsidP="00D33B61">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00D33B61">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>毕业要求指标点</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="4261" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="45ACC903" w14:textId="77777777" w:rsidR="002462A8" w:rsidRPr="00D33B61" w:rsidRDefault="002462A8" w:rsidP="00D33B61">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00D33B61">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>达成度</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#if conclusionList??]
                        [#list conclusionList as conclusion]
                        <w:tr w:rsidR="002462A8" w:rsidRPr="00D33B61" w14:paraId="6D381709" w14:textId="77777777" w:rsidTr="00D33B61">
                            <w:trPr>
                                <w:trHeight w:val="654"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="4261" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="22AF9C68" w14:textId="77777777" w:rsidR="002462A8" w:rsidRPr="00D33B61" w:rsidRDefault="002462A8" w:rsidP="00D33B61">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:sz w:val="24"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00D33B61">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if conclusion.graduateIndexNum?? && (conclusion.graduateIndexNum>=0)]${conclusion.graduateIndexNum}[/#if][#if conclusion.indicationIndexNum?? && (conclusion.indicationIndexNum >= 0)].${conclusion.indicationIndexNum}[/#if][#if conclusion.indicationContent??]${conclusion.indicationContent}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="4261" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w14:paraId="460FF508" w14:textId="77777777" w:rsidR="002462A8" w:rsidRPr="00D33B61" w:rsidRDefault="002462A8" w:rsidP="00D33B61">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:sz w:val="24"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="00D33B61">
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:b/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if conclusion.targetGradetotalTemp?? && conclusion.averageGradetotalTemp?? && conclusion.targetGradetotalTemp>0]${conclusion.averageGradetotalTemp/conclusion.targetGradetotalTemp}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [/#list]
                        [/#if]
                    </w:tbl>
                    <w:p w14:paraId="1BD89A06" w14:textId="77777777" w:rsidR="006B1664" w:rsidRPr="006B1664" w:rsidRDefault="006B1664" w:rsidP="002462A8">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w14:paraId="5F99A56E" w14:textId="77777777" w:rsidR="008A5382" w:rsidRPr="00B30DBB" w:rsidRDefault="006B1664" w:rsidP="006B1664">
                        <w:pPr>
                            <w:ind w:firstLineChars="200" w:firstLine="480"/>
                            <w:rPr>
                                <w:sz w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="006B1664">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>由</w:t>
                        </w:r>
                        <w:r w:rsidR="002462A8">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>表[#if tableCount??]${tableCount+2}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidRPr="006B1664">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>可知，</w:t>
                        </w:r>
                        [#if conclusionList??]
                        [#list conclusionList]
                        [#items as conclusion]
                        <w:r w:rsidRPr="006B1664">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>指标点</w:t>
                        </w:r>
                        <w:r w:rsidR="007210ED">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>[#if conclusion.graduateIndexNum?? && (conclusion.graduateIndexNum>=0)]${conclusion.graduateIndexNum}[/#if][#if conclusion.indicationIndexNum?? && (conclusion.indicationIndexNum >= 0)].${conclusion.indicationIndexNum}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidRPr="006B1664">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>达到了课程目标值</w:t>
                        </w:r>
                        <w:r w:rsidR="007210ED">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>[#if conclusion.targetGradetotalTemp?? && conclusion.averageGradetotalTemp?? && conclusion.targetGradetotalTemp>0]${conclusion.averageGradetotalTemp/conclusion.targetGradetotalTemp}[/#if]</w:t>
                        </w:r>
                        [#sep ]
                        <w:r w:rsidRPr="006B1664">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>；</w:t>
                        </w:r>
                        [/#items]
                        [/#list]
                        [/#if]
                        <w:r w:rsidRPr="006B1664">
                            <w:rPr>
                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                            </w:rPr>
                            <w:t>。</w:t>
                        </w:r>
                    </w:p>
                    <w:sectPr w:rsidR="008A5382" w:rsidRPr="00B30DBB" w:rsidSect="00D81F63">
                        <w:headerReference w:type="default" r:id="rId8"/>
                        <w:pgSz w:w="11906" w:h="16838"/>
                        <w:pgMar w:top="1440" w:right="1800" w:bottom="1440" w:left="1800" w:header="851" w:footer="992" w:gutter="0"/>
                        <w:cols w:space="425"/>
                        <w:docGrid w:type="lines" w:linePitch="312"/>
                    </w:sectPr>
                    [/#if]
                </w:body>
            </w:document>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/footnotes.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.footnotes+xml">
        <pkg:xmlData>
            <w:footnotes mc:Ignorable="w14 w15 wp14" 
                xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" 
                xmlns:mo="http://schemas.microsoft.com/office/mac/office/2008/main" 
                xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" 
                xmlns:mv="urn:schemas-microsoft-com:mac:vml" 
                xmlns:o="urn:schemas-microsoft-com:office:office" 
                xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" 
                xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" 
                xmlns:v="urn:schemas-microsoft-com:vml" 
                xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" 
                xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" 
                xmlns:w10="urn:schemas-microsoft-com:office:word" 
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" 
                xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" 
                xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml" 
                xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" 
                xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" 
                xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" 
                xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape">
                <w:footnote w:type="separator" w:id="-1">
                    <w:p w14:paraId="0B9D6282" w14:textId="77777777" w:rsidR="001A3BB7" w:rsidRDefault="001A3BB7">
                        <w:r>
                            <w:separator/>
                        </w:r>
                    </w:p>
                </w:footnote>
                <w:footnote w:type="continuationSeparator" w:id="0">
                    <w:p w14:paraId="295F2129" w14:textId="77777777" w:rsidR="001A3BB7" w:rsidRDefault="001A3BB7">
                        <w:r>
                            <w:continuationSeparator/>
                        </w:r>
                    </w:p>
                </w:footnote>
            </w:footnotes>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/endnotes.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.endnotes+xml">
        <pkg:xmlData>
            <w:endnotes mc:Ignorable="w14 w15 wp14" 
                xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" 
                xmlns:mo="http://schemas.microsoft.com/office/mac/office/2008/main" 
                xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" 
                xmlns:mv="urn:schemas-microsoft-com:mac:vml" 
                xmlns:o="urn:schemas-microsoft-com:office:office" 
                xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" 
                xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" 
                xmlns:v="urn:schemas-microsoft-com:vml" 
                xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" 
                xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" 
                xmlns:w10="urn:schemas-microsoft-com:office:word" 
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" 
                xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" 
                xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml" 
                xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" 
                xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" 
                xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" 
                xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape">
                <w:endnote w:type="separator" w:id="-1">
                    <w:p w14:paraId="1713A104" w14:textId="77777777" w:rsidR="001A3BB7" w:rsidRDefault="001A3BB7">
                        <w:r>
                            <w:separator/>
                        </w:r>
                    </w:p>
                </w:endnote>
                <w:endnote w:type="continuationSeparator" w:id="0">
                    <w:p w14:paraId="273F76D8" w14:textId="77777777" w:rsidR="001A3BB7" w:rsidRDefault="001A3BB7">
                        <w:r>
                            <w:continuationSeparator/>
                        </w:r>
                    </w:p>
                </w:endnote>
            </w:endnotes>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/header1.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml">
        <pkg:xmlData>
            <w:hdr mc:Ignorable="w14 w15 wp14" 
                xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" 
                xmlns:mo="http://schemas.microsoft.com/office/mac/office/2008/main" 
                xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" 
                xmlns:mv="urn:schemas-microsoft-com:mac:vml" 
                xmlns:o="urn:schemas-microsoft-com:office:office" 
                xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" 
                xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" 
                xmlns:v="urn:schemas-microsoft-com:vml" 
                xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" 
                xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" 
                xmlns:w10="urn:schemas-microsoft-com:office:word" 
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" 
                xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" 
                xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml" 
                xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" 
                xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" 
                xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" 
                xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape">
                <w:p w14:paraId="1A7CCC31" w14:textId="77777777" w:rsidR="000C69A5" w:rsidRDefault="000C69A5" w:rsidP="00812B26">
                    <w:pPr>
                        <w:pStyle w:val="a4"/>
                        <w:pBdr>
                            <w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                        </w:pBdr>
                    </w:pPr>
                </w:p>
            </w:hdr>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/theme/theme1.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.theme+xml">
        <pkg:xmlData>
            <a:theme name="Office 主题" 
                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                <a:themeElements>
                    <a:clrScheme name="Office">
                        <a:dk1>
                            <a:sysClr val="windowText" lastClr="000000"/>
                        </a:dk1>
                        <a:lt1>
                            <a:sysClr val="window" lastClr="FFFFFF"/>
                        </a:lt1>
                        <a:dk2>
                            <a:srgbClr val="44546A"/>
                        </a:dk2>
                        <a:lt2>
                            <a:srgbClr val="E7E6E6"/>
                        </a:lt2>
                        <a:accent1>
                            <a:srgbClr val="4472C4"/>
                        </a:accent1>
                        <a:accent2>
                            <a:srgbClr val="ED7D31"/>
                        </a:accent2>
                        <a:accent3>
                            <a:srgbClr val="A5A5A5"/>
                        </a:accent3>
                        <a:accent4>
                            <a:srgbClr val="FFC000"/>
                        </a:accent4>
                        <a:accent5>
                            <a:srgbClr val="5B9BD5"/>
                        </a:accent5>
                        <a:accent6>
                            <a:srgbClr val="70AD47"/>
                        </a:accent6>
                        <a:hlink>
                            <a:srgbClr val="0563C1"/>
                        </a:hlink>
                        <a:folHlink>
                            <a:srgbClr val="954F72"/>
                        </a:folHlink>
                    </a:clrScheme>
                    <a:fontScheme name="Office">
                        <a:majorFont>
                            <a:latin typeface="DengXian Light" panose="020F0302020204030204"/>
                            <a:ea typeface=""/>
                            <a:cs typeface=""/>
                            <a:font script="Jpan" typeface="Yu Gothic Light"/>
                            <a:font script="Hang" typeface="맑은 고딕"/>
                            <a:font script="Hans" typeface="DengXian Light"/>
                            <a:font script="Hant" typeface="新細明體"/>
                            <a:font script="Arab" typeface="Times New Roman"/>
                            <a:font script="Hebr" typeface="Times New Roman"/>
                            <a:font script="Thai" typeface="Angsana New"/>
                            <a:font script="Ethi" typeface="Nyala"/>
                            <a:font script="Beng" typeface="Vrinda"/>
                            <a:font script="Gujr" typeface="Shruti"/>
                            <a:font script="Khmr" typeface="MoolBoran"/>
                            <a:font script="Knda" typeface="Tunga"/>
                            <a:font script="Guru" typeface="Raavi"/>
                            <a:font script="Cans" typeface="Euphemia"/>
                            <a:font script="Cher" typeface="Plantagenet Cherokee"/>
                            <a:font script="Yiii" typeface="Microsoft Yi Baiti"/>
                            <a:font script="Tibt" typeface="Microsoft Himalaya"/>
                            <a:font script="Thaa" typeface="MV Boli"/>
                            <a:font script="Deva" typeface="Mangal"/>
                            <a:font script="Telu" typeface="Gautami"/>
                            <a:font script="Taml" typeface="Latha"/>
                            <a:font script="Syrc" typeface="Estrangelo Edessa"/>
                            <a:font script="Orya" typeface="Kalinga"/>
                            <a:font script="Mlym" typeface="Kartika"/>
                            <a:font script="Laoo" typeface="DokChampa"/>
                            <a:font script="Sinh" typeface="Iskoola Pota"/>
                            <a:font script="Mong" typeface="Mongolian Baiti"/>
                            <a:font script="Viet" typeface="Times New Roman"/>
                            <a:font script="Uigh" typeface="Microsoft Uighur"/>
                            <a:font script="Geor" typeface="Sylfaen"/>
                        </a:majorFont>
                        <a:minorFont>
                            <a:latin typeface="DengXian" panose="020F0502020204030204"/>
                            <a:ea typeface=""/>
                            <a:cs typeface=""/>
                            <a:font script="Jpan" typeface="Yu Mincho"/>
                            <a:font script="Hang" typeface="맑은 고딕"/>
                            <a:font script="Hans" typeface="DengXian"/>
                            <a:font script="Hant" typeface="新細明體"/>
                            <a:font script="Arab" typeface="Arial"/>
                            <a:font script="Hebr" typeface="Arial"/>
                            <a:font script="Thai" typeface="Cordia New"/>
                            <a:font script="Ethi" typeface="Nyala"/>
                            <a:font script="Beng" typeface="Vrinda"/>
                            <a:font script="Gujr" typeface="Shruti"/>
                            <a:font script="Khmr" typeface="DaunPenh"/>
                            <a:font script="Knda" typeface="Tunga"/>
                            <a:font script="Guru" typeface="Raavi"/>
                            <a:font script="Cans" typeface="Euphemia"/>
                            <a:font script="Cher" typeface="Plantagenet Cherokee"/>
                            <a:font script="Yiii" typeface="Microsoft Yi Baiti"/>
                            <a:font script="Tibt" typeface="Microsoft Himalaya"/>
                            <a:font script="Thaa" typeface="MV Boli"/>
                            <a:font script="Deva" typeface="Mangal"/>
                            <a:font script="Telu" typeface="Gautami"/>
                            <a:font script="Taml" typeface="Latha"/>
                            <a:font script="Syrc" typeface="Estrangelo Edessa"/>
                            <a:font script="Orya" typeface="Kalinga"/>
                            <a:font script="Mlym" typeface="Kartika"/>
                            <a:font script="Laoo" typeface="DokChampa"/>
                            <a:font script="Sinh" typeface="Iskoola Pota"/>
                            <a:font script="Mong" typeface="Mongolian Baiti"/>
                            <a:font script="Viet" typeface="Arial"/>
                            <a:font script="Uigh" typeface="Microsoft Uighur"/>
                            <a:font script="Geor" typeface="Sylfaen"/>
                        </a:minorFont>
                    </a:fontScheme>
                    <a:fmtScheme name="Office">
                        <a:fillStyleLst>
                            <a:solidFill>
                                <a:schemeClr val="phClr"/>
                            </a:solidFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="110000"/>
                                            <a:satMod val="105000"/>
                                            <a:tint val="67000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="50000">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="105000"/>
                                            <a:satMod val="103000"/>
                                            <a:tint val="73000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="105000"/>
                                            <a:satMod val="109000"/>
                                            <a:tint val="81000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="5400000" scaled="0"/>
                            </a:gradFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:satMod val="103000"/>
                                            <a:lumMod val="102000"/>
                                            <a:tint val="94000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="50000">
                                        <a:schemeClr val="phClr">
                                            <a:satMod val="110000"/>
                                            <a:lumMod val="100000"/>
                                            <a:shade val="100000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="99000"/>
                                            <a:satMod val="120000"/>
                                            <a:shade val="78000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="5400000" scaled="0"/>
                            </a:gradFill>
                        </a:fillStyleLst>
                        <a:lnStyleLst>
                            <a:ln w="6350" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                                <a:miter lim="800000"/>
                            </a:ln>
                            <a:ln w="12700" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                                <a:miter lim="800000"/>
                            </a:ln>
                            <a:ln w="19050" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                                <a:miter lim="800000"/>
                            </a:ln>
                        </a:lnStyleLst>
                        <a:effectStyleLst>
                            <a:effectStyle>
                                <a:effectLst/>
                            </a:effectStyle>
                            <a:effectStyle>
                                <a:effectLst/>
                            </a:effectStyle>
                            <a:effectStyle>
                                <a:effectLst>
                                    <a:outerShdw blurRad="57150" dist="19050" dir="5400000" algn="ctr" rotWithShape="0">
                                        <a:srgbClr val="000000">
                                            <a:alpha val="63000"/>
                                        </a:srgbClr>
                                    </a:outerShdw>
                                </a:effectLst>
                            </a:effectStyle>
                        </a:effectStyleLst>
                        <a:bgFillStyleLst>
                            <a:solidFill>
                                <a:schemeClr val="phClr"/>
                            </a:solidFill>
                            <a:solidFill>
                                <a:schemeClr val="phClr">
                                    <a:tint val="95000"/>
                                    <a:satMod val="170000"/>
                                </a:schemeClr>
                            </a:solidFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="93000"/>
                                            <a:satMod val="150000"/>
                                            <a:shade val="98000"/>
                                            <a:lumMod val="102000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="50000">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="98000"/>
                                            <a:satMod val="130000"/>
                                            <a:shade val="90000"/>
                                            <a:lumMod val="103000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:shade val="63000"/>
                                            <a:satMod val="120000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="5400000" scaled="0"/>
                            </a:gradFill>
                        </a:bgFillStyleLst>
                    </a:fmtScheme>
                </a:themeElements>
                <a:objectDefaults/>
                <a:extraClrSchemeLst/>
                <a:extLst>
                    <a:ext uri="{05A4C25C-085E-4340-85A3-A5531E510DB2}">
                        <thm15:themeFamily name="Office Theme" id="{62F939B6-93AF-4DB8-9C6B-D6C7DFDC589F}" vid="{4A3C46E8-61CC-4603-A589-7422A47A8E4A}" 
                            xmlns:thm15="http://schemas.microsoft.com/office/thememl/2012/main"/>
                    </a:ext>
                </a:extLst>
            </a:theme>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/media/image1.png" pkg:contentType="image/png" pkg:compression="store">
        <pkg:binaryData>iVBORw0KGgoAAAANSUhEUgAAAeQAAAEMCAIAAACa21JBAAAAAXNSR0IArs4c6QAAAAlwSFlzAAAS dAAAEnQB3mYfeAAAGoZJREFUeF7t3T1X6j4Ax/HgawEHD6+gd9AVXJxc2coIi5vn6ODmAiNsrkws wiqDvAKOg/S9cJOWh7aCtDStTfj2nP/DuTZp8kn93ZqkIlarleBAAAEEECixgAzqivqnov5d4nbS NAQQQOB8BYKIvjhfAHqOAAIImCNAWJszVrQUAQTOWICwPuPBp+sIIGCOAGFtzljRUgQQOGMBwvqM B5+uI4CAOQKEtTljRUsRQOCMBQjrMx58uo4AAuYIENbmjBUtRQCBMxYgrM948Ok6AgiYI0BYmzNW tBQBBM5YgLA+48Gn6wggYI4AYW3OWNFSBBA4YwHC+owHn64jgIA5AoS1OWNFSxFA4IwFCOszHny6 jgAC5ggQ1uaMFS1FAIEzFiCsz3jw6ToCCJgjQFibM1a0FAEEzliAsD7jwafrCCBgjgBhbc5Y0VIE EDhjgYvZbHbG3afrCCCAgAECMqj5dHMDxokmIoDAOQvw6ebnPPr0HQEEDBNgztqwAaO5CCBwngKE 9XmOO71GAAHDBAhrwwaM5iKAwHkKENbnOe70GgEEDBMgrA0bMJqLAALnKaAzrKdtucPEP9rTdJq7 kpXKv74XKez1/22qPbX6dI3hbAQQQKB8AtrCWuZtc9FbruSx7C2aKfI6VFIWntS7tUheL8W9X2vo GDTKB0mLEEAAgTwFNIX1tN0cOr23TlW1tdp56znDZrLHa6//MnQnn0FJeTQGy57ovqZ8NM+TiLoR QACBvxfQE9bT8VA497ebwBXV23tHDMcJEtd7H82dq1oYonpZT1b27/VoAQIIIFCQgJaw9r4XkayW D9cycMXiOzr7nLRLtStnV9b7/kpajvMQQAABawW0/G6Q9azzbi5DCLksWOvWJ6uj08uqrIicp/5k KNz1n3n9/rJzOVZ/5B9Obxm+TrqBkSuUPws8Pz+nq4WzEUAAgQwCT09PqUoHvxvkz8Na+Nm8iWYV 8q2RmM/nu7COpP6evxdS9Fr2+ePjI0UBTkUAAQS0CsgIOi2stUyDZOpKY7CauMPmelteS7x9Psop lO08drXzuQo9nzcGE3fO+mMmcQojgICBAlrCWk0yx47l13wXuMdcZF5vDjnFoWbARf1yu1oZK73n Ysfq5+sIIICA6QJawlotJ85H76HlxCOB+5ua2h8i3Lv1Vmr/dZlkmwBNHwrajwACCBwW0BLWonHn RtI6GrhH/FUe716D8ffy9R7WWe2H/ja5VUV79voxvggggID1AnrCWr7KoqaSW8Gb4l6/1ZULhNuN IMEL4/HXyPfSTtu1rti8XCPPqHYe3fAWwKDqx+0rNNaPDx1EAAEEfAFNYa3ePPTfFFfrhMn27G0H IAh6v6S/iy+6M0/W+yZam98KkrJqBhkBBBCwREDL1j1jLNi6Z8xQ0VAELBUweOuepSNCtxBAAAGd AtqmQXQ2iroQQAABBKIChDV3BAIIIGCAAGFtwCDRRAQQQOBiNpuhgAACCCBQZgEZ1BfX19dlbiJt QwABBBCQQc00CLcBAgggYIAAYW3AINFEBBBAgLDmHkAAAQQMECCsDRgkmogAAggQ1twDCCCAgAEC hLUBg0QTEUAAAcKaewABBBAwQICwNmCQaCICCCBAWHMPIIAAAgYIENYGDBJNRAABBAhr7gEEEEDA AAHC2oBBookIIIAAYc09gAACCBggQFgbMEg0EQEEECCsuQcQQAABAwQIawMGiSYigAAChDX3AAII IGCAAGFtwCDRRAQQQICw5h5AAAEEDBAgrA0YJJqIAAIIENbcAwgggIABAoS1AYNEExFAAIHiwnra rqyP9jSd+65kpfKv76UrzNkIIICADQIFhbXM2+ait1zJY9lbNCvJ8zpUUhae1Ls18tqGG48+IIBA OoGL2WyWrsQJZ0/bzaHTe+tUVdlq563nDJvJ4trrvwzdyWdQUh6NwbInuq8pH81PaDJFEEAAgfII yKC+uL6+zrtB0/FQOPe3m8AV1dt7RwzHCRLXex/NnatauIHVy3qysnn3ivoRQACBogRkUBcwDeJ9 LyJZLR+uZeCKxfdps8+1K+fkskXBch0EEEBAt4CcCJZVqsnkvI6JK4QTzFdvjmXPEcKdHL+iKhs9 T/1JsrJ7ateNR30IIIBAOoHn5+fjyRc9I4joivqnov6d7oIpzl4vEe4mnoXw+v9q3fpkNWgcq0cV lrPW6zNludZIzOfz7Z8cKx/9uuzpx8dHujKcjYCxAjc3N4NBft/apXBptw37ppYR9PT0lMouiOgC pkFStSp+cmOwmrjD5nrTX0u8fT7KKZTYPHamK1AYAQQQMECggLBWk8yxY/k1Tx64Mq83h3w6VzPg on65Xa00wJgmIoAAApkFCghrtZw4H72HlhMzBK7aHyLcu6PTJ5lhqAABBBAok0ABYS0ad24krVMF rnp9cfcajL+Xr/dAVpfpHqItCCBQgEARYS1fZZm4824reFPc67e6coFwu7YoFw3ljHSi1xKn7VpX bF6uKQCHSyCAAAJlESgkrNWbh/6b4mqdMOE2kA1QEPR+yUpTTFbhTSVlQaQdCCCAQN4CBYW17MZu nTC6Ya/a+ZTrh79kcGiB8fhWv7y9qB8BBBD4E4HiwvpPusdFEUAAATsECGs7xpFeIICA5QKEteUD TPcQQMAOAcLajnGkFwggYLkAYW35ANM9BBCwQ4CwtmMc6QUCCFguQFhbPsB0DwEE7BAgrO0YR3qB AAKWCxDWlg8w3UMAATsECGs7xpFeIICA5QKEteUDTPcQQMAOAcLajnGkFwggYLkAYW35ANM9BBCw Q4CwtmMc6QUCCFgucDGbzSzvIt1DAAEEDBeQQX1xfX1teC9oPgIIIGC5gAxqpkEsH2O6hwACdggQ 1naMI71AAAHLBQhryweY7iGAgB0ChLUd40gvEEDAcgHC2vIBpnsIIGCHAGFtxzjSCwQQsFyAsLZ8 gOkeAgjYIUBY2zGO9AIBBCwXIKwtH2C6hwACdggQ1naMI71AAAHLBQhryweY7iGAgB0ChLUd40gv EEDAcgGdYT1tV9ZHe5qOzev/2xStVP71vXDpyNeCs9JWn64xnI0AAgiUT0BbWMukbi56y5U8lr1F M0WgypK10b1fUh2TercWzuul2H1tfcqgUT5IWoQAAgjkKaAprKft5tDpvXWqqq3VzlvPGTaTPf96 /ZddSVW6MZi48+5rymfzPI2oGwEEEPhzAT1hPR0PhXN/60e1H9e3944YjpMGbv1yW1KVrl05f+5C AxBAAIFSCWgJa+97EclqmdaXdSEW35HJ5/39Vrk+fIlOUwvh3m1mOrzvr1KB0RgEEEDgLwQqchpY LtnJf2e4+nq++jOYBfEPuSxY69Ynq0TTy5GTvX77/Xawrcrr95edy3GlOQwqdnrL8HXStVr2NF0B zkbAcIHBIMu3tgGdb7cN+6Z+fn5+enpKJRtEdCnCWkZ7+12IbtcPZDcS8bHU3/P3Qopeyz5/fHyk KMCpCJgscHNzcw5hbdY3tWztaWGtZRok2+0s87clHjqdQbAZxB1GtpJUO5+r0PM5y4/ZsCmNAAKG CmgJ6z0rgsuvuXCuasdV5G6QxWYbiTq7MfDz+vBWEpYfj6NyBgIIWCegJazVcuJ89B5aTlRLjiK2 yWOvnfc+msfPa9y528VJ/0WbZJsArRsbOoQAAghsBbSEtVDxGk5rFcGhHR2/eKucj+8a8ZM+OPz/ 3W0NUX8iq070yM4gI4AAAhYJ6Anr9ZssrWAHntdvdefuZLsRJHhhPPYW+caw8dAT3Vro2XnarsnS j5v3ax53T9nbqtdftGgY6AoCCCDwu8DFbDbTYqTmmuWL4upXdyTfs6eurFYQl1cv298N4r+0vtvx J+t9E63NV9NVraVjVIIAAgj8tYAMai1b9/66H4mvz9a9xFScaIMAW/dKOIomb90rISdNQgABBEom oGnOumS9ojkIIICAZQKEtWUDSncQQMBOAcLaznGlVwggYJkAYW3ZgNIdBBCwU4CwtnNc6RUCCFgm QFhbNqB0BwEE7BQgrO0cV3qFAAKWCRDWlg0o3UEAATsFCGs7x5VeIYCAZQKEtWUDSncQQMBOAcLa znGlVwggYJkAYW3ZgNIdBBCwU4CwtnNc6RUCCFgmQFhbNqB0BwEE7BQgrO0cV3qFAAKWCRDWlg0o 3UEAATsFCGs7x5VeIYCAZQKEtWUDSncQQMBOAcLaznGlVwggYJkAYW3ZgNIdBBCwU4CwtnNc6RUC CFgmQFhbNqB0BwEE7BQgrO0cV3qFAAKWCVQ+Pj5ubm5Wq5VlHdvbnUpF9deUnspxMaWpWdpp0Ihk 6eaflJW30GBg+bd2u23SN7W8DeQN//T0lOp+CIKrImNa/h9hncqumJP5TivG2eKrcAuVcHBPC2sZ 0UyDlHA0aRICCCAQFygurKdt+QTvH+1punHw+v82RSuVf30vXWnORgABBCwQKCisZVI3F72lfJRf LXuLZoq8liVro3u/pDom9W6NvLbgxqMLCCCQTqCQsJ62m0On99apqrZVO289Z9hM9njt9V92JVXp xmDizruvKZ/N05lwNgIIIFA6gSLCejoeCuf+1o9qP65v7x0xHCcN3PrltqQqXbtySqdIgxBAAIGc BQoIa+97EclqmdaXdSEW3wkmn1WuD1/i09TuXSNnFqpHAAEEyiVQwNa99Xz1ZzAL4h9yybDWrU9W gyShGznZ67ffbwehqlJxynXKVOf/+cnnsEn2z5HtbgC3UNnG9/n5+YR91mqPdf77rLOGtYz29rsQ 3e5QqbsJI37vCBn3Usw5fKfxUkx+acI+6/xsT67Z3n3WMupb4qHTGQSbQdxhmq0kJ3tSEAEEECiX QAFz1ntWBJdfc+Fc1Y5TyN0gi802EnV2Y+DndbKtJMer5wwEEEDAEIECwlotJ85H76HlRLXkKGKb PPZ6ee+jefy8xp2bbHHSkBGgmQgggEACgQLCWqh4Dae1imCRaEeHyvn4rhE/6TkQQACB8xIoIqzX b7K0gh14Xr/VnbuT7UaQ4GXyA28lNh56olsLzXpM2zVZ+vHU7SDnNbj0FgEE7BEoJKyDuWb5orj6 FR/J9+wp5Wrnc7W8etn+bhD/pfVEO/7sGSN6ggACCIiCwlpKy7xeH9GsVXG8WoV3YceHJThjffx2 IuOJAAII2CpQXFjbKki/EEAAgQIECOsCkLkEAgggkFWAsM4qSHkEEECgAAHCugBkLoEAAghkFSCs swpSHgEEEChAgLAuAJlLIIAAAlkFCOusgpRHAAEEChAgrAtA5hIIIIBAVgHCOqsg5RFAAIECBC5m s1kBl+ESCCCAAAInC8igvri+vj65PAURQAABBAoQkEHNNEgBzlwCAQQQyCpAWGcVpDwCCCBQgABh XQAyl0AAAQSyChDWWQUpjwACCBQgQFgXgMwlEEAAgawChHVWQcojgAACBQgQ1gUgcwkEEEAgqwBh nVWQ8ggggEABAoR1AchcAgEEEMgqQFhnFaQ8AgggUIAAYV0AMpdAAAEEsgoQ1lkFKY8AAggUIEBY F4DMJRBAAIGsAoR1VkHKI4AAAgUIENYFIHMJBBBAIKsAYZ1VkPIIIIBAAQKEdQHIXMIegZubG3s6 Q0/+QuD5+fm0y+oM62m7sj7a0xSt2RXbFJf//df31lV4/X+hP/f/N1X1KVrCqQgggEBZBbSFtYzc 5qK3XMlj2Vs0kweq971wgnKbY9lze2+d6ppsKe4jX5VnDRpl5aRdCCCAQD4CmsJ62m4OnU3CVjtv PWfYTPj8K8N4l8yyk16/9XW3jep8ek2tCCCAgGECesJ6Oh4K5/528zAsqrf3jhiOE02GNDqRZJ6+ ju4feHI27DaiuQggkLeAlrCWMxmRrBaielkXYvG9mXdO3Auv/yIeI+HtfX8lLs2JCCCAgLUCcgpY 9i08ZZz+/yeuED/mnR0h3EnKuiZurBo1Ad6brNQF1sePE9JcwtpRpGMIIGCOQJrQUucGEa3lyVoT knysXoTmUja1jpqV8d2md5N6t7bbKZL2wmmNOB8BBBDQLpA2uILzL2az2WkltZfy3kfiZ1ZXO5/h 7R+NwcSdd18TzYZrbyEVIoAAAn8iIIP64vr6OvO1a1dyziN6LL/mwrmqpah6+tqd1y+3a5QHS+65 WIqrcCoCCCBgnoAMai3TIGo5cT56Dy0nqiVHkSR6t2j+IuXPdPffmEm4CdC8AaDFCCCAQEIBLWEt GnduJK3ljMZcuHdpduCpR/H51zLWbD/0IxWpqtM9sieU4DQEEECgxAJ6wloEU8mt4B1x+VpLd+5O tu8ZBi+M/74s6Kfyz6PaeXTDWwCDqqOb+0qsS9MQQAABTQKawlrIuF6pnRrqV3fUuvVJ2lfC/Y3Z +w5Z75tobX47yClVa5KiGgQQQOAPBSpyV4pMwmArHwcCCCCAQNkEgojW9mRdtu7RHgQQQMAmAcLa ptGkLwggYK0AYW3t0NIxBBCwSYCwtmk06QsCCFgrQFhbO7R0DAEEbBIgrG0aTfqCAALWChDW1g4t HUMAAZsECGubRpO+IICAtQKEtbVDS8cQQMAmAcLaptGkLwggYK0AYW3t0NIxBBCwSYCwtmk06QsC CFgrQFhbO7R0DAEEbBIgrG0aTfqCAALWChDWZRxa/+Madp/WoD7bLOlHuseKZumdxqqyNIOyiQS4 ZxIxmXwSYV3W0Yt+Ho5Tvww3VMX3wU+mdHpvnd8+eDjN51ryqTxlvT/2tYt7xqTRSt1Wwjo12d8U uKqt89d/gmoOhSu+g09R++0IPlEtdozv5C8yvxPTY4X3fV1V+MvnF8svJ/0R4JSrUyaVgBn3TKou nfXJhHWph9/zwnnsPxK3xJvM2tVqMOhcLo8FbrXz6Z8cOfwPx2w0En2c8bAZTXp19bvxgbj2PyHz JE8Z8nyE/UlyPwuZdM8cHnf/XveP1DfGrmjy2UNN9vlWQ1jn65uq9u1dVpOZ58dkrVYLbta5+nhL /5H489cpjs311BOwjmdcdxINenX1xmD7WciR7k1fv+puqg7Lz1YOnvxVhzlOETDznjky7rJTzUVv qW69ZW/RTJHXquKXK7+kX1h0azq+DU4ZGf1lLmazmf5aqfEkAfnhwJubzBHbmHyofQvhqFs3lpHe 9/h78+Ad/p5Vwa6egN/E++Enb6/fP/ZYnq4P0/b4bnCXroxYP/kve07Kgpy+FjDznvl13Kft5nC7 7lLtvPWcYTPh4/X0tStCKzZ+4fno/eh0oQG3kwzqi+vrawNaes5NrFbl2uL8axk38N5Hw25tfRdv vmdV7KlgV0/Ay69ubBIjNKVR68ov/vgWCP8E6T/uxqdBDj2m+FGdaGLlnIeysL4bcc8c0JiOh8K5 v90ukVdv7x0xHCd9tIh+p1Qv64Wh53shGdRMg+RLnK329XJeYzBx46npTx3Ip+9oQsoAn6spEz+F tw9d6x8JgxgPH/viNX7O7nz5F4FTF8s9jylEdbZx1lnalHvmUJ+970Ukq4XwA3ex/SHyGFYk11Vt 9cvf9kYdq65EXyesSzQYkaaodFaTGcFy4GaCZM9SYaiUn9Uqku/GJyzMJJG4um38uPO9/vjqgafq JHw5n2POPXMYYvn1Y/miduXs+9FyTx3+90noEcSfFrHm3iSsc/7+SV29ejJSExBqznq9mLhnv9ze F1ZkVgvXVdO/jQc50ef/5BhfadQ+V+31X8VDokXP1BQUSChg2j2TsFtZT5OT32KSbEE+66UKKU9Y F8Kc6CLBEnnt61GuYv9YcItNg7RG8gk6NLGnLiCzuv74cOVfS63gqCeM4Fl7tFtprHYuxylW1481 nKg+JpTv1028Z/IV2dQul1/k7imr1lEI62JunSRXkWvXk8nywP0V20P3+fhj4UTunHuM35sqq93J 52cnNEshZ8BF8/B+Jn+T4N5DPu/H1znlj5lykXN3tnxZZ10+4fJ9EhfOOSxg4D1zZDjVnEfsUDMj zlUtxX1gYVLL3hPWKW6B3E+tNn7OCCe8qNf//rkdQ2ZpffLz4UJOksj9pwfi9NcFRvcuOjcdn0uf uJtFTKseaRKOwZ+cZtw9c0RJLSdGd9upRcI0q4R2JjVh/SffXiddNL4bRD7Dhg41G3EbX+Pz+i9i T1T7kySPrhi+xF9X96bi4ZeXbuTUyokvqZ/UYwplFTDznmncuZG09mfy4k8JB2nkvFBs9kP7Kk3W YTm5PE/WJ9MVWzD+KqF8hg0d1c5gt8YXzFWo/H47+HyrftiMb2mavo5FsNUj8is+5IPK9iG8IX7d ZuI/A0X/ElHT8EffIVM/5ybfm1UsvMFXK/09s3/c1U7VebcVPEr4v8LA3T1zBJP0h7f7yw1U0Zt+ +joSaWZQSjzehHWJBydD044t/G0WIHeXkM/hi828oHzyrofetwnNcTcG6v3fn98r64UuuYslmLNO OGW9fu1Y/ZjAXHeG4dZStMB75si4y9m1ibwBg19EUI+/THC4s/J9mh9LLs2hNfushdyXKDv/87f9 8Cd/IBDaBRKeOj7wNvbe2WV1buSJKlw4/qi17aL/nB6pbzf7rH7FQrjOzUuSf+DDJX8KcM+cwV0R RDRhXb6hlt9+0VRd9tzYq4er1WQS+xVL636o2N2XyRP38Mqh/ytvevvrKx8PLdojwD1j9W0RhHVF /VNR/9bykxSVIIAAAgjoFQgimjlrvarUhgACCOQiQFjnwkqlCCCAgF4BwlqvJ7UhgAACuQgQ1rmw UikCCCCgV4Cw1utJbQgggEAuAoR1LqxUigACCOgVIKz1elIbAgggkIsAYZ0LK5UigAACegUIa72e 1IYAAgjkIkBY58JKpQgggIBeAcJarye1IYAAArkIENa5sFIpAgggoFfgYjab6a2R2hBAAAEE9ArI oOa37uklpTYEEEBAswC/dU8zKNUhgAAC+QkwZ52fLTUjgAAC2gQIa22UVIQAAgjkJ0BY52dLzQgg gIA2AcJaGyUVIYAAAvkJENb52VIzAgggoE2AsNZGSUUIIIBAfgKEdX621IwAAghoEyCstVFSEQII IJCjwGq1yrF2qkYAAQQQyCwgg/o/pR3TvOhu3gQAAAAASUVORK5CYII=</pkg:binaryData>
    </pkg:part>
    <pkg:part pkg:name="/word/settings.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml">
        <pkg:xmlData>
            <w:settings mc:Ignorable="w14 w15" 
                xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" 
                xmlns:o="urn:schemas-microsoft-com:office:office" 
                xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" 
                xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" 
                xmlns:v="urn:schemas-microsoft-com:vml" 
                xmlns:w10="urn:schemas-microsoft-com:office:word" 
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" 
                xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" 
                xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml" 
                xmlns:sl="http://schemas.openxmlformats.org/schemaLibrary/2006/main">
                <w:zoom w:percent="110"/>
                <w:bordersDoNotSurroundHeader/>
                <w:bordersDoNotSurroundFooter/>
                <w:proofState w:spelling="clean" w:grammar="clean"/>
                <w:stylePaneFormatFilter w:val="3F01" w:allStyles="1" w:customStyles="0" w:latentStyles="0" w:stylesInUse="0" w:headingStyles="0" w:numberingStyles="0" w:tableStyles="0" w:directFormattingOnRuns="1" w:directFormattingOnParagraphs="1" w:directFormattingOnNumbering="1" w:directFormattingOnTables="1" w:clearFormatting="1" w:top3HeadingStyles="1" w:visibleStyles="0" w:alternateStyleNames="0"/>
                <w:doNotTrackMoves/>
                <w:defaultTabStop w:val="420"/>
                <w:drawingGridVerticalSpacing w:val="156"/>
                <w:displayHorizontalDrawingGridEvery w:val="0"/>
                <w:displayVerticalDrawingGridEvery w:val="2"/>
                <w:characterSpacingControl w:val="compressPunctuation"/>
                <w:hdrShapeDefaults>
                    <o:shapedefaults v:ext="edit" spidmax="2049"/>
                </w:hdrShapeDefaults>
                <w:footnotePr>
                    <w:footnote w:id="-1"/>
                    <w:footnote w:id="0"/>
                </w:footnotePr>
                <w:endnotePr>
                    <w:endnote w:id="-1"/>
                    <w:endnote w:id="0"/>
                </w:endnotePr>
                <w:compat>
                    <w:spaceForUL/>
                    <w:balanceSingleByteDoubleByteWidth/>
                    <w:doNotLeaveBackslashAlone/>
                    <w:ulTrailSpace/>
                    <w:doNotExpandShiftReturn/>
                    <w:adjustLineHeightInTable/>
                    <w:useFELayout/>
                    <w:useNormalStyleForList/>
                    <w:doNotUseIndentAsNumberingTabStop/>
                    <w:useAltKinsokuLineBreakRules/>
                    <w:allowSpaceOfSameStyleInTable/>
                    <w:doNotSuppressIndentation/>
                    <w:doNotAutofitConstrainedTables/>
                    <w:autofitToFirstFixedWidthCell/>
                    <w:displayHangulFixedWidth/>
                    <w:splitPgBreakAndParaMark/>
                    <w:doNotVertAlignCellWithSp/>
                    <w:doNotBreakConstrainedForcedTable/>
                    <w:doNotVertAlignInTxbx/>
                    <w:useAnsiKerningPairs/>
                    <w:cachedColBalance/>
                    <w:compatSetting w:name="compatibilityMode" w:uri="http://schemas.microsoft.com/office/word" w:val="11"/>
                </w:compat>
                <w:rsids>
                    <w:rsidRoot w:val="00287C05"/>
                    <w:rsid w:val="00010C4F"/>
                    <w:rsid w:val="00021E57"/>
                    <w:rsid w:val="000339CB"/>
                    <w:rsid w:val="00043BDE"/>
                    <w:rsid w:val="00045758"/>
                    <w:rsid w:val="00057987"/>
                    <w:rsid w:val="00060A89"/>
                    <w:rsid w:val="0007100B"/>
                    <w:rsid w:val="00084E70"/>
                    <w:rsid w:val="000A10A0"/>
                    <w:rsid w:val="000A6CC0"/>
                    <w:rsid w:val="000B79E4"/>
                    <w:rsid w:val="000C03A4"/>
                    <w:rsid w:val="000C69A5"/>
                    <w:rsid w:val="000D51C8"/>
                    <w:rsid w:val="000E0BD6"/>
                    <w:rsid w:val="000E1EA9"/>
                    <w:rsid w:val="000E6D60"/>
                    <w:rsid w:val="000E79A6"/>
                    <w:rsid w:val="000F115B"/>
                    <w:rsid w:val="000F4361"/>
                    <w:rsid w:val="00112F99"/>
                    <w:rsid w:val="0012006F"/>
                    <w:rsid w:val="00133219"/>
                    <w:rsid w:val="00154045"/>
                    <w:rsid w:val="001649EB"/>
                    <w:rsid w:val="00181C04"/>
                    <w:rsid w:val="00194764"/>
                    <w:rsid w:val="001A3BB7"/>
                    <w:rsid w:val="001B0590"/>
                    <w:rsid w:val="001B455F"/>
                    <w:rsid w:val="001C204E"/>
                    <w:rsid w:val="001C3025"/>
                    <w:rsid w:val="001E54A1"/>
                    <w:rsid w:val="001E6076"/>
                    <w:rsid w:val="001F1661"/>
                    <w:rsid w:val="001F1965"/>
                    <w:rsid w:val="002017FC"/>
                    <w:rsid w:val="00204D9B"/>
                    <w:rsid w:val="00211F43"/>
                    <w:rsid w:val="00216274"/>
                    <w:rsid w:val="0022579D"/>
                    <w:rsid w:val="00236513"/>
                    <w:rsid w:val="0024470D"/>
                    <w:rsid w:val="002601A4"/>
                    <w:rsid w:val="002613B8"/>
                    <w:rsid w:val="002841CE"/>
                    <w:rsid w:val="00286FE3"/>
                    <w:rsid w:val="00287C05"/>
                    <w:rsid w:val="002946DF"/>
                    <w:rsid w:val="00296C5A"/>
                    <w:rsid w:val="002E1121"/>
                    <w:rsid w:val="002E56D0"/>
                    <w:rsid w:val="00300588"/>
                    <w:rsid w:val="00306DEE"/>
                    <w:rsid w:val="00313AC4"/>
                    <w:rsid w:val="00340BC8"/>
                    <w:rsid w:val="00361925"/>
                    <w:rsid w:val="00365690"/>
                    <w:rsid w:val="0038335F"/>
                    <w:rsid w:val="003A1E96"/>
                    <w:rsid w:val="003C16C0"/>
                    <w:rsid w:val="003C791C"/>
                    <w:rsid w:val="003D410F"/>
                    <w:rsid w:val="003D6DD9"/>
                    <w:rsid w:val="003E1533"/>
                    <w:rsid w:val="003E3B0B"/>
                    <w:rsid w:val="0040412D"/>
                    <w:rsid w:val="004100EE"/>
                    <w:rsid w:val="004105AA"/>
                    <w:rsid w:val="00416496"/>
                    <w:rsid w:val="0043515E"/>
                    <w:rsid w:val="0044668B"/>
                    <w:rsid w:val="00451834"/>
                    <w:rsid w:val="00452DEA"/>
                    <w:rsid w:val="00455194"/>
                    <w:rsid w:val="004606C8"/>
                    <w:rsid w:val="00483201"/>
                    <w:rsid w:val="00492BBC"/>
                    <w:rsid w:val="004A1BC7"/>
                    <w:rsid w:val="004C5B7F"/>
                    <w:rsid w:val="004E1209"/>
                    <w:rsid w:val="004F4036"/>
                    <w:rsid w:val="0050676D"/>
                    <w:rsid w:val="005226C1"/>
                    <w:rsid w:val="00522D33"/>
                    <w:rsid w:val="005235A5"/>
                    <w:rsid w:val="005460FE"/>
                    <w:rsid w:val="00554F62"/>
                    <w:rsid w:val="005752C4"/>
                    <w:rsid w:val="00576E03"/>
                    <w:rsid w:val="00592717"/>
                    <w:rsid w:val="005950F7"/>
                    <w:rsid w:val="005C0EB5"/>
                    <w:rsid w:val="005F0B9E"/>
                    <w:rsid w:val="005F30D7"/>
                    <w:rsid w:val="00626761"/>
                    <w:rsid w:val="00635293"/>
                    <w:rsid w:val="00651D94"/>
                    <w:rsid w:val="0065527A"/>
                    <w:rsid w:val="0066156E"/>
                    <w:rsid w:val="0066627A"/>
                    <w:rsid w:val="00694CB7"/>
                    <w:rsid w:val="006B1664"/>
                    <w:rsid w:val="006B37A1"/>
                    <w:rsid w:val="006B6A62"/>
                    <w:rsid w:val="006C21ED"/>
                    <w:rsid w:val="006D7B2A"/>
                    <w:rsid w:val="006E26DF"/>
                    <w:rsid w:val="007051F3"/>
                    <w:rsid w:val="0070608D"/>
                    <w:rsid w:val="00706F6D"/>
                    <w:rsid w:val="007210ED"/>
                    <w:rsid w:val="007413F3"/>
                    <w:rsid w:val="007457BB"/>
                    <w:rsid w:val="00761230"/>
                    <w:rsid w:val="00774C80"/>
                    <w:rsid w:val="007774B8"/>
                    <w:rsid w:val="007A21DA"/>
                    <w:rsid w:val="007E5F75"/>
                    <w:rsid w:val="007E65EC"/>
                    <w:rsid w:val="008017F4"/>
                    <w:rsid w:val="00803BCB"/>
                    <w:rsid w:val="00805382"/>
                    <w:rsid w:val="0080713A"/>
                    <w:rsid w:val="00810440"/>
                    <w:rsid w:val="00812B26"/>
                    <w:rsid w:val="00813F15"/>
                    <w:rsid w:val="00822505"/>
                    <w:rsid w:val="00823283"/>
                    <w:rsid w:val="0083345E"/>
                    <w:rsid w:val="00855F27"/>
                    <w:rsid w:val="00870CBE"/>
                    <w:rsid w:val="00872381"/>
                    <w:rsid w:val="008833F0"/>
                    <w:rsid w:val="008A5382"/>
                    <w:rsid w:val="008B36D1"/>
                    <w:rsid w:val="008C1F13"/>
                    <w:rsid w:val="008C7506"/>
                    <w:rsid w:val="008E2C46"/>
                    <w:rsid w:val="008E30AF"/>
                    <w:rsid w:val="008E6AC2"/>
                    <w:rsid w:val="00933882"/>
                    <w:rsid w:val="00936749"/>
                    <w:rsid w:val="00936EA8"/>
                    <w:rsid w:val="0094047B"/>
                    <w:rsid w:val="00962276"/>
                    <w:rsid w:val="00971961"/>
                    <w:rsid w:val="009756DC"/>
                    <w:rsid w:val="00976486"/>
                    <w:rsid w:val="00980C53"/>
                    <w:rsid w:val="009915D4"/>
                    <w:rsid w:val="00993140"/>
                    <w:rsid w:val="009B07C4"/>
                    <w:rsid w:val="009B7CDD"/>
                    <w:rsid w:val="009C33F5"/>
                    <w:rsid w:val="009D1EB2"/>
                    <w:rsid w:val="009D693F"/>
                    <w:rsid w:val="009E2BB9"/>
                    <w:rsid w:val="009E6B2E"/>
                    <w:rsid w:val="009F08DB"/>
                    <w:rsid w:val="00A13DF4"/>
                    <w:rsid w:val="00A51DF9"/>
                    <w:rsid w:val="00A52110"/>
                    <w:rsid w:val="00A547DF"/>
                    <w:rsid w:val="00A55721"/>
                    <w:rsid w:val="00A71236"/>
                    <w:rsid w:val="00A911C3"/>
                    <w:rsid w:val="00AB513F"/>
                    <w:rsid w:val="00AC31B4"/>
                    <w:rsid w:val="00AD4E21"/>
                    <w:rsid w:val="00AD5994"/>
                    <w:rsid w:val="00AD720C"/>
                    <w:rsid w:val="00AE1621"/>
                    <w:rsid w:val="00AE195B"/>
                    <w:rsid w:val="00AF3C1A"/>
                    <w:rsid w:val="00AF6A33"/>
                    <w:rsid w:val="00AF7D27"/>
                    <w:rsid w:val="00B1118D"/>
                    <w:rsid w:val="00B22239"/>
                    <w:rsid w:val="00B30DBB"/>
                    <w:rsid w:val="00B44955"/>
                    <w:rsid w:val="00B45C06"/>
                    <w:rsid w:val="00B55A90"/>
                    <w:rsid w:val="00B708E4"/>
                    <w:rsid w:val="00B72602"/>
                    <w:rsid w:val="00B7522A"/>
                    <w:rsid w:val="00B75F25"/>
                    <w:rsid w:val="00BD19B4"/>
                    <w:rsid w:val="00BE2EC5"/>
                    <w:rsid w:val="00BE61C0"/>
                    <w:rsid w:val="00C01B30"/>
                    <w:rsid w:val="00C0491D"/>
                    <w:rsid w:val="00C113F9"/>
                    <w:rsid w:val="00C205A3"/>
                    <w:rsid w:val="00C307B8"/>
                    <w:rsid w:val="00C31A5C"/>
                    <w:rsid w:val="00C3546A"/>
                    <w:rsid w:val="00C5116B"/>
                    <w:rsid w:val="00C52545"/>
                    <w:rsid w:val="00C5261D"/>
                    <w:rsid w:val="00C638ED"/>
                    <w:rsid w:val="00C639EA"/>
                    <w:rsid w:val="00C64019"/>
                    <w:rsid w:val="00C778D0"/>
                    <w:rsid w:val="00CA0621"/>
                    <w:rsid w:val="00CD4D50"/>
                    <w:rsid w:val="00CF7601"/>
                    <w:rsid w:val="00CF7DD5"/>
                    <w:rsid w:val="00D11E32"/>
                    <w:rsid w:val="00D20C99"/>
                    <w:rsid w:val="00D37205"/>
                    <w:rsid w:val="00D41C4B"/>
                    <w:rsid w:val="00D65042"/>
                    <w:rsid w:val="00D66CCE"/>
                    <w:rsid w:val="00D75F81"/>
                    <w:rsid w:val="00D81F63"/>
                    <w:rsid w:val="00D82063"/>
                    <w:rsid w:val="00D91A70"/>
                    <w:rsid w:val="00D94FF6"/>
                    <w:rsid w:val="00DA37ED"/>
                    <w:rsid w:val="00DA47A3"/>
                    <w:rsid w:val="00DC234D"/>
                    <w:rsid w:val="00DE2D2C"/>
                    <w:rsid w:val="00E33F48"/>
                    <w:rsid w:val="00E37218"/>
                    <w:rsid w:val="00E405B2"/>
                    <w:rsid w:val="00E7768B"/>
                    <w:rsid w:val="00EA3930"/>
                    <w:rsid w:val="00EB55C3"/>
                    <w:rsid w:val="00EC51EA"/>
                    <w:rsid w:val="00EF3073"/>
                    <w:rsid w:val="00EF5279"/>
                    <w:rsid w:val="00EF6EB8"/>
                    <w:rsid w:val="00F05044"/>
                    <w:rsid w:val="00F058DE"/>
                    <w:rsid w:val="00F07789"/>
                    <w:rsid w:val="00F15064"/>
                    <w:rsid w:val="00F15A7B"/>
                    <w:rsid w:val="00F22C71"/>
                    <w:rsid w:val="00F303A5"/>
                    <w:rsid w:val="00F33E4B"/>
                    <w:rsid w:val="00F40EE3"/>
                    <w:rsid w:val="00F42D4F"/>
                    <w:rsid w:val="00F446FA"/>
                    <w:rsid w:val="00F72A62"/>
                    <w:rsid w:val="00F808F5"/>
                    <w:rsid w:val="00FA69F6"/>
                    <w:rsid w:val="00FC40BC"/>
                    <w:rsid w:val="00FC49BF"/>
                    <w:rsid w:val="00FE2E0E"/>
                    <w:rsid w:val="00FF1D20"/>
                </w:rsids>
                <m:mathPr>
                    <m:mathFont m:val="Cambria Math"/>
                    <m:brkBin m:val="before"/>
                    <m:brkBinSub m:val="--"/>
                    <m:smallFrac m:val="0"/>
                    <m:dispDef/>
                    <m:lMargin m:val="0"/>
                    <m:rMargin m:val="0"/>
                    <m:defJc m:val="centerGroup"/>
                    <m:wrapIndent m:val="1440"/>
                    <m:intLim m:val="subSup"/>
                    <m:naryLim m:val="undOvr"/>
                </m:mathPr>
                <w:themeFontLang w:val="en-US" w:eastAsia="zh-CN"/>
                <w:clrSchemeMapping w:bg1="light1" w:t1="dark1" w:bg2="light2" w:t2="dark2" w:accent1="accent1" w:accent2="accent2" w:accent3="accent3" w:accent4="accent4" w:accent5="accent5" w:accent6="accent6" w:hyperlink="hyperlink" w:followedHyperlink="followedHyperlink"/>
                <w:doNotIncludeSubdocsInStats/>
                <w:shapeDefaults>
                    <o:shapedefaults v:ext="edit" spidmax="2049"/>
                    <o:shapelayout v:ext="edit">
                        <o:idmap v:ext="edit" data="1"/>
                    </o:shapelayout>
                </w:shapeDefaults>
                <w:decimalSymbol w:val="."/>
                <w:listSeparator w:val=","/>
                <w14:docId w14:val="569E5C85"/>
                <w15:chartTrackingRefBased/>
                <w15:docId w15:val="{FFFFF82B-A1D2-465B-82CE-BF2A1CA965C2}"/>
            </w:settings>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/webSettings.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.webSettings+xml">
        <pkg:xmlData>
            <w:webSettings mc:Ignorable="w14 w15" 
                xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" 
                xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" 
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" 
                xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" 
                xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml">
                <w:divs>
                    <w:div w:id="68889699">
                        <w:bodyDiv w:val="1"/>
                        <w:marLeft w:val="0"/>
                        <w:marRight w:val="0"/>
                        <w:marTop w:val="0"/>
                        <w:marBottom w:val="0"/>
                        <w:divBdr>
                            <w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                        </w:divBdr>
                    </w:div>
                    <w:div w:id="864829713">
                        <w:bodyDiv w:val="1"/>
                        <w:marLeft w:val="0"/>
                        <w:marRight w:val="0"/>
                        <w:marTop w:val="0"/>
                        <w:marBottom w:val="0"/>
                        <w:divBdr>
                            <w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                        </w:divBdr>
                    </w:div>
                    <w:div w:id="1001810990">
                        <w:bodyDiv w:val="1"/>
                        <w:marLeft w:val="0"/>
                        <w:marRight w:val="0"/>
                        <w:marTop w:val="0"/>
                        <w:marBottom w:val="0"/>
                        <w:divBdr>
                            <w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                        </w:divBdr>
                    </w:div>
                    <w:div w:id="1218786503">
                        <w:bodyDiv w:val="1"/>
                        <w:marLeft w:val="0"/>
                        <w:marRight w:val="0"/>
                        <w:marTop w:val="0"/>
                        <w:marBottom w:val="0"/>
                        <w:divBdr>
                            <w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                        </w:divBdr>
                        <w:divsChild>
                            <w:div w:id="651787321">
                                <w:marLeft w:val="0"/>
                                <w:marRight w:val="0"/>
                                <w:marTop w:val="0"/>
                                <w:marBottom w:val="0"/>
                                <w:divBdr>
                                    <w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                                    <w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                                    <w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                                    <w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                                </w:divBdr>
                            </w:div>
                        </w:divsChild>
                    </w:div>
                </w:divs>
                <w:doNotSaveAsSingleFile/>
                <w:pixelsPerInch w:val="96"/>
                <w:targetScreenSz w:val="800x600"/>
            </w:webSettings>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/styles.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml">
        <pkg:xmlData>
            <w:styles mc:Ignorable="w14 w15" 
                xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" 
                xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" 
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" 
                xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" 
                xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml">
                <w:docDefaults>
                    <w:rPrDefault>
                        <w:rPr>
                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="宋体" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                        </w:rPr>
                    </w:rPrDefault>
                    <w:pPrDefault/>
                </w:docDefaults>
                <w:latentStyles w:defLockedState="0" w:defUIPriority="0" w:defSemiHidden="0" w:defUnhideWhenUsed="0" w:defQFormat="0" w:count="382">
                    <w:lsdException w:name="Normal" w:qFormat="1"/>
                    <w:lsdException w:name="heading 1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 2" w:semiHidden="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 3" w:semiHidden="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 4" w:semiHidden="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 5" w:semiHidden="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 6" w:semiHidden="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 7" w:semiHidden="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 8" w:semiHidden="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 9" w:semiHidden="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="caption" w:semiHidden="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="Title" w:qFormat="1"/>
                    <w:lsdException w:name="Subtitle" w:qFormat="1"/>
                    <w:lsdException w:name="Strong" w:qFormat="1"/>
                    <w:lsdException w:name="Emphasis" w:qFormat="1"/>
                    <w:lsdException w:name="Note Level 1" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Note Level 2" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Note Level 3" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Note Level 4" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Note Level 5" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Note Level 6" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Note Level 7" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Note Level 8" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Note Level 9" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Placeholder Text" w:semiHidden="1" w:uiPriority="99"/>
                    <w:lsdException w:name="No Spacing" w:uiPriority="1" w:qFormat="1"/>
                    <w:lsdException w:name="Light Shading" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 1" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 1" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 1" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 1" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 1" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 1" w:uiPriority="65"/>
                    <w:lsdException w:name="Revision" w:semiHidden="1" w:uiPriority="99"/>
                    <w:lsdException w:name="List Paragraph" w:uiPriority="34" w:qFormat="1"/>
                    <w:lsdException w:name="Quote" w:uiPriority="29" w:qFormat="1"/>
                    <w:lsdException w:name="Intense Quote" w:uiPriority="30" w:qFormat="1"/>
                    <w:lsdException w:name="Medium List 2 Accent 1" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 1" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 1" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 1" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 1" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 1" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 1" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 1" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 2" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 2" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 2" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 2" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 2" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 2" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 2" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 2" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 2" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 2" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 2" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 2" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 2" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 2" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 3" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 3" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 3" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 3" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 3" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 3" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 3" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 3" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 3" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 3" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 3" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 3" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 3" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 3" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 4" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 4" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 4" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 4" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 4" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 4" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 4" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 4" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 4" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 4" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 4" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 4" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 4" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 4" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 5" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 5" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 5" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 5" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 5" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 5" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 5" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 5" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 5" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 5" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 5" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 5" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 5" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 5" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 6" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 6" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 6" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 6" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 6" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 6" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 6" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 6" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 6" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 6" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 6" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 6" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 6" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 6" w:uiPriority="73"/>
                    <w:lsdException w:name="Subtle Emphasis" w:uiPriority="19" w:qFormat="1"/>
                    <w:lsdException w:name="Intense Emphasis" w:uiPriority="21" w:qFormat="1"/>
                    <w:lsdException w:name="Subtle Reference" w:uiPriority="31" w:qFormat="1"/>
                    <w:lsdException w:name="Intense Reference" w:uiPriority="32" w:qFormat="1"/>
                    <w:lsdException w:name="Book Title" w:uiPriority="33" w:qFormat="1"/>
                    <w:lsdException w:name="Bibliography" w:semiHidden="1" w:uiPriority="37" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="TOC Heading" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="Plain Table 1" w:uiPriority="41"/>
                    <w:lsdException w:name="Plain Table 2" w:uiPriority="42"/>
                    <w:lsdException w:name="Plain Table 3" w:uiPriority="43"/>
                    <w:lsdException w:name="Plain Table 4" w:uiPriority="44"/>
                    <w:lsdException w:name="Plain Table 5" w:uiPriority="45"/>
                    <w:lsdException w:name="Grid Table Light" w:uiPriority="40"/>
                    <w:lsdException w:name="Grid Table 1 Light" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 1" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 1" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 1" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 1" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 1" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 1" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 1" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 2" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 2" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 2" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 2" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 2" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 2" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 2" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 3" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 3" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 3" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 3" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 3" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 3" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 3" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 4" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 4" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 4" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 4" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 4" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 4" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 4" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 5" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 5" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 5" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 5" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 5" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 5" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 5" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 6" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 6" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 6" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 6" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 6" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 6" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 6" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 1" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 1" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 1" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 1" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 1" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 1" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 1" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 2" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 2" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 2" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 2" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 2" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 2" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 2" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 3" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 3" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 3" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 3" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 3" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 3" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 3" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 4" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 4" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 4" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 4" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 4" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 4" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 4" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 5" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 5" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 5" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 5" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 5" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 5" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 5" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 6" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 6" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 6" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 6" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 6" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 6" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 6" w:uiPriority="52"/>
                    <w:lsdException w:name="Mention" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Smart Hyperlink" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
                </w:latentStyles>
                <w:style w:type="paragraph" w:default="1" w:styleId="a">
                    <w:name w:val="Normal"/>
                    <w:qFormat/>
                    <w:pPr>
                        <w:widowControl w:val="0"/>
                        <w:jc w:val="both"/>
                    </w:pPr>
                    <w:rPr>
                        <w:kern w:val="2"/>
                        <w:sz w:val="21"/>
                        <w:szCs w:val="24"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:default="1" w:styleId="a0">
                    <w:name w:val="Default Paragraph Font"/>
                    <w:semiHidden/>
                </w:style>
                <w:style w:type="table" w:default="1" w:styleId="a1">
                    <w:name w:val="Normal Table"/>
                    <w:semiHidden/>
                    <w:tblPr>
                        <w:tblInd w:w="0" w:type="dxa"/>
                        <w:tblCellMar>
                            <w:top w:w="0" w:type="dxa"/>
                            <w:left w:w="108" w:type="dxa"/>
                            <w:bottom w:w="0" w:type="dxa"/>
                            <w:right w:w="108" w:type="dxa"/>
                        </w:tblCellMar>
                    </w:tblPr>
                </w:style>
                <w:style w:type="numbering" w:default="1" w:styleId="a2">
                    <w:name w:val="No List"/>
                    <w:semiHidden/>
                </w:style>
                <w:style w:type="table" w:styleId="a3">
                    <w:name w:val="Table Grid"/>
                    <w:basedOn w:val="a1"/>
                    <w:rsid w:val="00C778D0"/>
                    <w:pPr>
                        <w:widowControl w:val="0"/>
                        <w:jc w:val="both"/>
                    </w:pPr>
                    <w:tblPr>
                        <w:tblInd w:w="0" w:type="dxa"/>
                        <w:tblBorders>
                            <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:insideH w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:insideV w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                        </w:tblBorders>
                        <w:tblCellMar>
                            <w:top w:w="0" w:type="dxa"/>
                            <w:left w:w="108" w:type="dxa"/>
                            <w:bottom w:w="0" w:type="dxa"/>
                            <w:right w:w="108" w:type="dxa"/>
                        </w:tblCellMar>
                    </w:tblPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a4">
                    <w:name w:val="header"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="a5"/>
                    <w:rsid w:val="006C21ED"/>
                    <w:pPr>
                        <w:pBdr>
                            <w:bottom w:val="single" w:sz="6" w:space="1" w:color="auto"/>
                        </w:pBdr>
                        <w:tabs>
                            <w:tab w:val="center" w:pos="4153"/>
                            <w:tab w:val="right" w:pos="8306"/>
                        </w:tabs>
                        <w:snapToGrid w:val="0"/>
                        <w:jc w:val="center"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Calibri" w:hAnsi="Calibri"/>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="a5">
                    <w:name w:val="页眉字符"/>
                    <w:link w:val="a4"/>
                    <w:locked/>
                    <w:rsid w:val="006C21ED"/>
                    <w:rPr>
                        <w:rFonts w:ascii="Calibri" w:eastAsia="宋体" w:hAnsi="Calibri"/>
                        <w:kern w:val="2"/>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                        <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a6">
                    <w:name w:val="footer"/>
                    <w:basedOn w:val="a"/>
                    <w:rsid w:val="001B0590"/>
                    <w:pPr>
                        <w:tabs>
                            <w:tab w:val="center" w:pos="4153"/>
                            <w:tab w:val="right" w:pos="8306"/>
                        </w:tabs>
                        <w:snapToGrid w:val="0"/>
                        <w:jc w:val="left"/>
                    </w:pPr>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="Char1">
                    <w:name w:val=" Char1"/>
                    <w:basedOn w:val="a7"/>
                    <w:rsid w:val="0070608D"/>
                    <w:pPr>
                        <w:shd w:val="clear" w:color="auto" w:fill="000080"/>
                        <w:adjustRightInd w:val="0"/>
                        <w:spacing w:line="436" w:lineRule="exact"/>
                        <w:ind w:left="357"/>
                        <w:jc w:val="left"/>
                        <w:outlineLvl w:val="3"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman"/>
                        <w:sz w:val="21"/>
                        <w:szCs w:val="20"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a7">
                    <w:name w:val="Document Map"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="a8"/>
                    <w:rsid w:val="0070608D"/>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体"/>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="a8">
                    <w:name w:val="文档结构图字符"/>
                    <w:link w:val="a7"/>
                    <w:rsid w:val="0070608D"/>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体"/>
                        <w:kern w:val="2"/>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
            </w:styles>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/fontTable.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml">
        <pkg:xmlData>
            <w:fonts mc:Ignorable="w14 w15" 
                xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" 
                xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" 
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" 
                xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" 
                xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml">
                <w:font w:name="Times New Roman">
                    <w:panose1 w:val="02020603050405020304"/>
                    <w:charset w:val="00"/>
                    <w:family w:val="roman"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="E0002AEF" w:usb1="C0007841" w:usb2="00000009" w:usb3="00000000" w:csb0="000001FF" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="宋体">
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="00000003" w:usb1="288F0000" w:usb2="00000016" w:usb3="00000000" w:csb0="00040001" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Calibri">
                    <w:panose1 w:val="020F0502020204030204"/>
                    <w:charset w:val="00"/>
                    <w:family w:val="swiss"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="E00002FF" w:usb1="4000ACFF" w:usb2="00000001" w:usb3="00000000" w:csb0="0000019F" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="黑体">
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="fixed"/>
                    <w:sig w:usb0="800002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000" w:csb0="00040001" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Arial">
                    <w:panose1 w:val="020B0604020202020204"/>
                    <w:charset w:val="00"/>
                    <w:family w:val="swiss"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="E0002AFF" w:usb1="C0007843" w:usb2="00000009" w:usb3="00000000" w:csb0="000001FF" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="DengXian Light">
                    <w:panose1 w:val="02010600030101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="script"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="A00002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000" w:csb0="0004000F" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="DengXian">
                    <w:panose1 w:val="02010600030101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="script"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="A00002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000" w:csb0="0004000F" w:csb1="00000000"/>
                </w:font>
            </w:fonts>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/docProps/core.xml" pkg:contentType="application/vnd.openxmlformats-package.core-properties+xml" pkg:padding="256">
        <pkg:xmlData>
            <cp:coreProperties 
                xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" 
                xmlns:dc="http://purl.org/dc/elements/1.1/" 
                xmlns:dcterms="http://purl.org/dc/terms/" 
                xmlns:dcmitype="http://purl.org/dc/dcmitype/" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <dc:title>课程目标达成度评价</dc:title>
                <dc:subject/>
                <dc:creator>lenovo</dc:creator>
                <cp:keywords/>
                <dc:description/>
                <cp:lastModifiedBy>余海伦</cp:lastModifiedBy>
                <cp:revision>2</cp:revision>
                <dcterms:created xsi:type="dcterms:W3CDTF">2017-10-18T06:47:00Z</dcterms:created>
                <dcterms:modified xsi:type="dcterms:W3CDTF">2017-10-18T06:47:00Z</dcterms:modified>
            </cp:coreProperties>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/numbering.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml">
        <pkg:xmlData>
            <w:numbering mc:Ignorable="w14 w15 wp14" 
                xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" 
                xmlns:mo="http://schemas.microsoft.com/office/mac/office/2008/main" 
                xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" 
                xmlns:mv="urn:schemas-microsoft-com:mac:vml" 
                xmlns:o="urn:schemas-microsoft-com:office:office" 
                xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" 
                xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" 
                xmlns:v="urn:schemas-microsoft-com:vml" 
                xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" 
                xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" 
                xmlns:w10="urn:schemas-microsoft-com:office:word" 
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" 
                xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" 
                xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml" 
                xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" 
                xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" 
                xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" 
                xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape">
                <w:abstractNum w:abstractNumId="0">
                    <w:nsid w:val="03672E11"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="7ED8A3D4"/>
                    <w:lvl w:ilvl="0" w:tplc="1A0A7BDE">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1）"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="360"/>
                            </w:tabs>
                            <w:ind w:left="360" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="840"/>
                            </w:tabs>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1260"/>
                            </w:tabs>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1680"/>
                            </w:tabs>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2100"/>
                            </w:tabs>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2520"/>
                            </w:tabs>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2940"/>
                            </w:tabs>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3360"/>
                            </w:tabs>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3780"/>
                            </w:tabs>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="1">
                    <w:nsid w:val="2C34016D"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="776E1A5E"/>
                    <w:lvl w:ilvl="0" w:tplc="C11CC84A">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1）"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="360"/>
                            </w:tabs>
                            <w:ind w:left="360" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="840"/>
                            </w:tabs>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1260"/>
                            </w:tabs>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1680"/>
                            </w:tabs>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2100"/>
                            </w:tabs>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2520"/>
                            </w:tabs>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2940"/>
                            </w:tabs>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3360"/>
                            </w:tabs>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3780"/>
                            </w:tabs>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="2">
                    <w:nsid w:val="2C6B493A"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="A8F669F0"/>
                    <w:lvl w:ilvl="0" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%1)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="420"/>
                            </w:tabs>
                            <w:ind w:left="420" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="840"/>
                            </w:tabs>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1260"/>
                            </w:tabs>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1680"/>
                            </w:tabs>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2100"/>
                            </w:tabs>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2520"/>
                            </w:tabs>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2940"/>
                            </w:tabs>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3360"/>
                            </w:tabs>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3780"/>
                            </w:tabs>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="3">
                    <w:nsid w:val="42E218C7"/>
                    <w:multiLevelType w:val="multilevel"/>
                    <w:tmpl w:val="D3DAF752"/>
                    <w:lvl w:ilvl="0">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="420"/>
                            </w:tabs>
                            <w:ind w:left="420" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="840"/>
                            </w:tabs>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1260"/>
                            </w:tabs>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1680"/>
                            </w:tabs>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2100"/>
                            </w:tabs>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2520"/>
                            </w:tabs>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2940"/>
                            </w:tabs>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3360"/>
                            </w:tabs>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3780"/>
                            </w:tabs>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="4">
                    <w:nsid w:val="4E0444F0"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="E4C015E8"/>
                    <w:lvl w:ilvl="0" w:tplc="2EC000CC">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="japaneseCounting"/>
                        <w:lvlText w:val="%1、"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="420"/>
                            </w:tabs>
                            <w:ind w:left="420" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="840"/>
                            </w:tabs>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1260"/>
                            </w:tabs>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1680"/>
                            </w:tabs>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2100"/>
                            </w:tabs>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2520"/>
                            </w:tabs>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2940"/>
                            </w:tabs>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3360"/>
                            </w:tabs>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3780"/>
                            </w:tabs>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="5">
                    <w:nsid w:val="565C4EBD"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="FAE279CA"/>
                    <w:lvl w:ilvl="0" w:tplc="C2026FEA">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="japaneseCounting"/>
                        <w:lvlText w:val="%1、"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="420"/>
                            </w:tabs>
                            <w:ind w:left="420" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="840"/>
                            </w:tabs>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1260"/>
                            </w:tabs>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1680"/>
                            </w:tabs>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2100"/>
                            </w:tabs>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2520"/>
                            </w:tabs>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2940"/>
                            </w:tabs>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3360"/>
                            </w:tabs>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3780"/>
                            </w:tabs>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="6">
                    <w:nsid w:val="6FDE6633"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="AC7C91AA"/>
                    <w:lvl w:ilvl="0" w:tplc="9312A3A6">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="japaneseCounting"/>
                        <w:lvlText w:val="%1、"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="420"/>
                            </w:tabs>
                            <w:ind w:left="420" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="840"/>
                            </w:tabs>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1260"/>
                            </w:tabs>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1680"/>
                            </w:tabs>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2100"/>
                            </w:tabs>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2520"/>
                            </w:tabs>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2940"/>
                            </w:tabs>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3360"/>
                            </w:tabs>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3780"/>
                            </w:tabs>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:num w:numId="1">
                    <w:abstractNumId w:val="6"/>
                </w:num>
                <w:num w:numId="2">
                    <w:abstractNumId w:val="4"/>
                </w:num>
                <w:num w:numId="3">
                    <w:abstractNumId w:val="5"/>
                </w:num>
                <w:num w:numId="4">
                    <w:abstractNumId w:val="2"/>
                </w:num>
                <w:num w:numId="5">
                    <w:abstractNumId w:val="3"/>
                </w:num>
                <w:num w:numId="6">
                    <w:abstractNumId w:val="1"/>
                </w:num>
                <w:num w:numId="7">
                    <w:abstractNumId w:val="0"/>
                </w:num>
            </w:numbering>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/docProps/app.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.extended-properties+xml" pkg:padding="256">
        <pkg:xmlData>
            <Properties 
                xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties" 
                xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">
                <Template>Normal.dotm</Template>
                <TotalTime>0</TotalTime>
                <Pages>10</Pages>
                <Words>793</Words>
                <Characters>4521</Characters>
                <Application>Microsoft Macintosh Word</Application>
                <DocSecurity>0</DocSecurity>
                <Lines>37</Lines>
                <Paragraphs>10</Paragraphs>
                <ScaleCrop>false</ScaleCrop>
                <HeadingPairs>
                    <vt:vector size="2" baseType="variant">
                        <vt:variant>
                            <vt:lpstr>标题</vt:lpstr>
                        </vt:variant>
                        <vt:variant>
                            <vt:i4>1</vt:i4>
                        </vt:variant>
                    </vt:vector>
                </HeadingPairs>
                <TitlesOfParts>
                    <vt:vector size="1" baseType="lpstr">
                        <vt:lpstr>课程目标达成度评价</vt:lpstr>
                    </vt:vector>
                </TitlesOfParts>
                <Company/>
                <LinksUpToDate>false</LinksUpToDate>
                <CharactersWithSpaces>5304</CharactersWithSpaces>
                <SharedDoc>false</SharedDoc>
                <HyperlinksChanged>false</HyperlinksChanged>
                <AppVersion>15.0000</AppVersion>
            </Properties>
        </pkg:xmlData>
    </pkg:part>
</pkg:package>