#!/usr/bin/perl -w

use strict;
use warnings;
use 5.010;
use Data::Dumper;
use Set::Object;
use Set::Object qw(set);
use IO::Handle;

sub printLog{
    my ($strToPrint)=@_;
    chomp $strToPrint;
    STDOUT->printflush("(".(localtime).") $strToPrint \n");
}

printLog "Enter the name of the sample file\n";
# my $sample_file = "gdc_sample_sheet.2018-06-06.tsv"; #<STDIN>;

# my $overall_sample_file = "mainSampleSheet.tsv"; # LONG FILE
# LONG FILE
my $overall_sample_file = "shortSampleSheet.tsv"; # SHORT FILE

printLog "Using file $overall_sample_file.\n";
my $sample_file = $overall_sample_file;

# open mapping file
open INPUT, $sample_file or die "Cannot find file $sample_file";

my @sample_id=();
my @data_type = (); #apothikeuei ola ta diaforetika eidi dedomenwn gia to kathe deigma
my $row_counter=0; 
# initialize an empty hash patient_file_association, value is case id and key is the associated folder and file which is unique 
#works ok!
my %patient_vital_status_association;
my %patient_daysToDeath_association;
my %patient_meth_file_association;
my %patient_mirna_file_association;
my %patient_rnaseq_file_association;
my %patient_data_association;
my %patient_type_of_data_association;
my $FULL_SET_OF_DATA_TYPES = Set::Object->new();
my $FULL_SET_OF_SAMPLES = Set::Object->new();

printLog "Reading Gene Annotation file...\n";
# Get overall symbols hash
sub get_rnasymbols_hash {
        use warnings;
        
	# Use fixed fle name for RNA Symbols Lookup
	my $input_file = "mart_export_versions.txt";
	# Create empty hashtable
	my %hashtable;

	open RNA_SYMBOLS_FILE,$input_file or die "Could not load RNA symbols lookup file ($input_file)\n";
	# For each line
	while (my $line=<RNA_SYMBOLS_FILE>) {
            # Ignore header
            if ($. > 1) {
                # Then read and split the next lines
		my @row=split("\t", $line);
		# Add key-value pair to hashtable
		chomp $row[0];
		chomp $row[1];
		
		$hashtable{ $row[0] } = $row[1];
            }
	}

	close RNA_SYMBOLS_FILE;
	
	# DEBUG OUTPUT
# 	printLog Dumper \%hashtable;
	##############
	my $size = keys %hashtable;
	printLog "Read ".$size." items.";
	
	# Return hashtable by ref
	return \%hashtable;
}

my $symbols_look_up_ref = get_rnasymbols_hash();
my %symbols_look_up = %$symbols_look_up_ref;

printLog "Reading Gene Annotation file... Done.\n";

printLog "Reading of data types per sample...\n";
#store folder id/file id, file_id/file name, data type, 
while (my $line = <INPUT>){
	
	if ($. > 1){
	
		my @column = split ("\t", $line);

		$data_type[$row_counter] = $column[3];
		
		$sample_id[$row_counter] = $column[5];
		
		
		$patient_type_of_data_association {$sample_id[$row_counter]} = undef;
		
		$row_counter++;

		
		
		
		@column =();
	}
	
}

$FULL_SET_OF_DATA_TYPES -> insert (@data_type);
$FULL_SET_OF_SAMPLES -> insert (@sample_id);

close INPUT;
printLog "Reading of data types per sample... Done.\n";


printLog "Filling in data types per sample...\n";
my @SET_OF_Sample_IDs =  @$FULL_SET_OF_SAMPLES;
my @datatype = ();
	
foreach my $sample (@SET_OF_Sample_IDs){
	
	open INPUT, $overall_sample_file or die "Cannot find file";
	#open CLIN, "clinical_test.txt" or die "Cannot find file";
	while (my $row = <INPUT>){
		
		if ($.>1){
		
			my @column = split ("\t", $row);

			my $data_type = $column[3];
			my $sample_id = $column[5];
			if ($sample eq $sample_id){
			
					push (@datatype, $data_type);
			
			}
		
		
		}

	
	}

	my $datatypes_conc = join (";", @datatype);
	$patient_type_of_data_association {$sample} = $datatypes_conc;
	
	close INPUT;
	@datatype = ();
}

printLog "Filling in data types per sample... Done.\n";

#printLog Dumper \%patient_type_of_data_association;


printLog "Extract data paths per sample... \n";
foreach my $sample (keys %patient_type_of_data_association){
	
	my $meth =0;
	my $rnaseq = 0;
	my $mirna =0;

	open INPUT, $overall_sample_file or die "Cannot find file";

	while (my $line = <INPUT>){
	
		if ($. > 1){
			
			my @column = split ("\t", $line);
			my $folder_id = $column[0];
			my $file_id = $column[1];
			my $sample_id = $column[5];
			my $data_type = $column[3];
			
			if ($sample_id eq $sample){
				
				if ( ($data_type eq 'Methylation Beta Value')&& $meth<=1) {
					
					my $path  = join ("//", $folder_id,$file_id);
					
					$patient_meth_file_association{$sample_id} = $path;
				
					$meth++;
				}

				elsif (($data_type eq 'miRNA Expression Quantification')&& $mirna<=1){
					
					my $path  = join ("//", $folder_id,$file_id);
					
					$patient_mirna_file_association{$sample_id} = $path;
					
					$mirna++;
				}

				elsif (($data_type eq 'Gene Expression Quantification')&& $rnaseq<=1){
					
					my $path  = join ("//", $folder_id,$file_id);
					
					$patient_rnaseq_file_association{$sample_id} = $path;
					
					$rnaseq++;
					
				}
					
			}
			
			@column =();
		}
	
	}

	
	close INPUT;

}
printLog "Extract data paths per sample... Done. \n";


#printLog "--------rna----\n";
#printLog Dumper \%patient_rnaseq_file_association;
#printLog "--------meth----\n";
#printLog Dumper \%patient_meth_file_association;
#printLog "--------mirna----\n";
#printLog Dumper \%patient_mirna_file_association;


get_all_data_for_all_patients(\%patient_rnaseq_file_association,\%patient_meth_file_association,\%patient_mirna_file_association,$FULL_SET_OF_DATA_TYPES,$FULL_SET_OF_SAMPLES);

#input ena hash poy tha sysxetizei astheneis me type of data

sub get_all_data_for_all_patients {

	use warnings;
	
	my($patient_mrna_file_association_ref,$patient_meth_file_association_ref,$patient_mirna_file_association_ref,$FULL_SET_OF_DATA_TYPES,$FULL_SET_OF_SAMPLES) = @_;
	my @set_of_data_types = @$FULL_SET_OF_DATA_TYPES;
	my @set_of_samples = @$FULL_SET_OF_SAMPLES;
	my %patient_mrna_file_association = %$patient_mrna_file_association_ref;
	my %patient_meth_file_association = %$patient_meth_file_association_ref;
	my %patient_mirna_file_association = %$patient_mirna_file_association_ref;
	my $patient_meth_data_association_hash_ref;
	my $patient_mirna_data_association_hash_ref;
	my $patient_rnaseq_data_association_hash_ref;
	my $data_type_length = @set_of_data_types;
	my $gene_sorted_list;
	my $mirna_sorted_list;
	my $gene_meth_sorted_list;
		
	
	for(my $counter =0; $counter<$data_type_length; $counter++){
	

			if ($set_of_data_types[$counter] eq 'Methylation Beta Value'){
				
				($patient_meth_data_association_hash_ref, $gene_meth_sorted_list) = get_gene_symbols_from_methylation_files (\%patient_meth_file_association);
			
			}

			elsif ($set_of_data_types[$counter] eq 'miRNA Expression Quantification'){

				($patient_mirna_data_association_hash_ref, $mirna_sorted_list) = get_mirna_id_from_mirna_files(\%patient_mirna_file_association);
			}

			elsif ($set_of_data_types[$counter] eq 'Gene Expression Quantification'){

				($patient_rnaseq_data_association_hash_ref, $gene_sorted_list)	= get_gene_symbols_from_rnaseq_files(\%patient_mrna_file_association);
				
			}
			
	}
	
	my @gene_symbol_sorted = @$gene_sorted_list;
	my %patient_meth_data_association = %$patient_meth_data_association_hash_ref;
	my @mirna_sorted = @$mirna_sorted_list;
	my %patient_mirna_data_association = %$patient_mirna_data_association_hash_ref;
	my @gene_meth_sorted = @$gene_meth_sorted_list;
	my %patient_rnaseq_data_association = %$patient_rnaseq_data_association_hash_ref;
	
	
	printLog "-----type of data---\n";
	printLog Dumper \%patient_type_of_data_association;
	printLog "-----meth---\n";
	printLog Dumper \%patient_meth_data_association;
	printLog "-----mirna---\n";
	printLog Dumper \%patient_mirna_data_association;
	printLog "-----mrna---\n";
	printLog Dumper \%patient_rnaseq_data_association;
	
	write_output(\%patient_meth_data_association,\%patient_mirna_data_association,\%patient_rnaseq_data_association, \@gene_symbol_sorted,\@mirna_sorted,\@gene_meth_sorted,\@set_of_samples);
	
}

#sub poy tha exei ws input 4 (mazi me ta klinika) hash enos deigmatos {key gene/gene/mirna => value } kai tis listes tous
#gia kathe stoixeio tis listas bres to key kai grapse se ena arxeio to value toy hash

sub write_output{
	
	no warnings;

	my($patient_meth_data_association_hash_ref,$patient_mirna_data_association_hash_ref,$patient_rna_data_association_hash_ref, $gene_sorted_list, $mirna_sorted_list, $gene_meth_sorted_list)= @_; #,#$all_samples) = @_;
	my @gene_symbol_list= @$gene_sorted_list;
	my @mirna_symbol_list= @$mirna_sorted_list;
	my @gene_meth_symbol_list= @$gene_meth_sorted_list;
	my %patient_meth_data_association = %$patient_meth_data_association_hash_ref;
	my %patient_mirna_data_association = %$patient_mirna_data_association_hash_ref;
	my %patient_rna_data_association = %$patient_rna_data_association_hash_ref;

	my $temp = 0;
	my $counter;
	
	printLog "Writing headers...";
	
	open WRITE, ">output.txt" or die "Cannot open file for output.";
	
	#printLog headers
	
	print WRITE "DATA\t";
	
	foreach my $element (@gene_symbol_list){

		print WRITE "m_$element\t";

	}
	foreach my $mirna (@mirna_symbol_list){

		print WRITE "$mirna\t";

	}
	foreach my $dna_meth (@gene_meth_symbol_list){

		print WRITE "$dna_meth\t";

	}
	
	print WRITE "Vital_status\t";
	print WRITE "Days_To_Death\t";
	
	printLog "Writing headers... Done.";

	my $gene_length = @gene_symbol_list;
	my $mirna_length = @mirna_symbol_list;
	my $meth_length = @gene_meth_symbol_list;
	
	my $instanceCount = 0;
	my $allInstancesCount = keys %patient_type_of_data_association;
	printLog "Writing instances...\n";

	foreach my $sample (keys %patient_type_of_data_association){
			
			printLog "Writing data for patient #$instanceCount / $allInstancesCount ($sample)...\n"; 
			my @data_type_per_sample = split(";",$patient_type_of_data_association{$sample});
			my $length = @data_type_per_sample;
			my $rnaseq = 0;
			my $mirna = 0;
			my $meth = 0;

			print WRITE "\n$sample\t";
			
			printLog "Updating RNA data...\n";
			# For each rna gene symbol
			foreach my $rnaCurrentGene (@gene_symbol_list) {
                            # if we have a corresponding record
                            if (exists $patient_rna_data_association{$sample} and exists $patient_rna_data_association{$sample}{$rnaCurrentGene}) {
                                # write it
                                print WRITE "$patient_rna_data_association{$sample}{$rnaCurrentGene}\t";
                            }
                            else {
                                # write na
                                print WRITE "na\t";
                            }
			}
			printLog "Updating RNA data... Done.\n";

			printLog "Updating miRNA data...\n";
			# For each mirna gene symbol
			foreach my $mirnaCurrentSymbol (@mirna_symbol_list) {
                            # if we have a corresponding record
                            if (exists $patient_mirna_data_association{$sample} and exists $patient_mirna_data_association{$sample}{$mirnaCurrentSymbol}) {
                                # write it
                                print WRITE "$patient_mirna_data_association{$sample}{$mirnaCurrentSymbol}\t";
                            }
                            else {
                                # write na
                                print WRITE "na\t";
                            }
			}
			printLog "Updating miRNA data... Done.\n";
			
			printLog "Updating methylation data...\n";
			# For each meth gene symbol
			foreach my $methCurrentSymbol (@gene_meth_symbol_list) {
                            # if we have a corresponding record
                            if (exists $patient_meth_data_association{$sample} and exists $patient_meth_data_association{$sample}{$methCurrentSymbol}) {
                                # write it
                                print WRITE "$patient_meth_data_association{$sample}{$methCurrentSymbol}\t";
                            }
                            else {
                                # write na
                                print WRITE "na\t";
                            }
			}
			printLog "Updating methylation data... Done.\n";
			
			
# 			# For every patient data type
# 			for (my $counter_type=0; $counter_type<$length; $counter_type++){
# 				# If we talk about gene expression
# 				if ($data_type_per_sample[$counter_type] eq 'Gene Expression Quantification'){
#                                         # Increase the number of times we faced gene expression
# 					$rnaseq++;
# 
# 				}
# 				elsif ($data_type_per_sample[$counter_type] eq 'miRNA Expression Quantification'){
# 				
# 					$mirna++;
# 
# 				}
# 				elsif($data_type_per_sample[$counter_type] eq 'Methylation Beta Value'){
# 				
# 					$meth++;
# 				}
# 			}	
# 			
# 			if ($rnaseq == 1){
# 			
# 				foreach my $rna_patient (keys %patient_rna_data_association){
# 				
# 					if ($rna_patient eq $sample){
# 						
# 						for ($counter = 0; $counter < $gene_length; $counter++ ){
# 							
# 							foreach my $gene ( keys %{$patient_rna_data_association{$rna_patient}}){
# 
# 									if($gene_symbol_list[$counter] eq $gene){
# 
# 										#printLog "\n $gene_symbol_list[$counter] equals $gene and has this value $patient_rna_data_association{$rna_patient}{$gene}\n";
# 										
# 										print WRITE "$patient_rna_data_association{$rna_patient}{$gene}\t";
# 									
# 									}
# 								
# 							}
# 
# 						}
# 					}
# 					
# 				
# 				}
# 			}
# 			elsif($rnaseq == 0){
# 
# 				for ($counter = 0; $counter < $gene_length; $counter++ ){
# 
# 							print WRITE "-\t";
# 
# 				}
# 			}

			
# 			if($mirna == 1){
# 			
# 					foreach my $mirna_patient (keys %patient_mirna_data_association){
# 					
# 						if ($mirna_patient eq $sample){
# 							
# 							for ($counter = 0; $counter < $mirna_length; $counter++ ){
# 						
# 								foreach my $mirna ( keys %{$patient_mirna_data_association{$mirna_patient}}){
# 									
# 									#printLog "mirna $mirna\n";
# 
# 										if($mirna_symbol_list[$counter] eq $mirna){
# 
# 											#printLog "\n $mirna_symbol_list[$counter] equals $mirna and has this value $patient_mirna_data_association{$mirna_patient}{$mirna}\n";
# 											
# 											print WRITE "$patient_mirna_data_association{$mirna_patient}{$mirna}\t";
# 										}
# 									
# 								}
# 					
# 							}
# 						}
# 					}
# 			}			
# 			elsif($mirna == 0){
# 
# 				for ($counter = 0; $counter < $mirna_length; $counter++ ){
# 
# 						print WRITE "-\t";
# 
# 				}
# 
# 			}
				
			
# 			if($meth == 1){
# 			
# 				foreach my $meth_patient (keys %patient_meth_data_association){
# 				
# 					if ($meth_patient eq $sample){
# 						
# 						for ($counter = 0; $counter < $meth_length; $counter++ ){
# 					
# 							foreach my $meth_gene ( keys %{$patient_meth_data_association{$meth_patient}}){
# 								
# 								#printLog "meth_gene $meth_gene\n";
# 
# 									if($gene_meth_symbol_list[$counter] eq $meth_gene){
# 
# 										#printLog "\n $gene_meth_symbol_list[$counter] equals $meth_gene and has this value $patient_meth_data_association{$meth_patient}{$meth_gene}\n";
# 										
# 										print WRITE "$patient_meth_data_association{$meth_patient}{$meth_gene}\t";
# 									}
# 								
# 								
# 							}
# 				
# 						}
# 					}
# 			
# 				}	
# 				
# 			}
# 			elsif($meth == 0){
# 				
# 				for ($counter = 0; $counter < $meth_length; $counter++ ){
# 
# 									print WRITE "-\t";
# 			
# 				}
# 			
# 			}

                        # Append clinical data
			open CLIN, "clinical.tsv" or die "Cannot find file";
			while (my $row = <CLIN>){
			
				if ($.>1){
					
					my @column = split ("\t", $row);
					my $submitter_id = $column[1];
					my $vital_status = $column[13];
					my $days_to_death = $column[15];
				

					if ($submitter_id eq $sample){
					
						print WRITE "$vital_status\t";
						print WRITE "$days_to_death\t";
				
					}
			
				}

			}
			close CLIN;

                printLog "Writing data for patient #$instanceCount / $allInstancesCount ($sample)... Done.\n"; 

				
	}
	
	
	close WRITE;
}


sub get_gene_symbols_from_rnaseq_files{
	
	use warnings;
	
	printLog "sub rna seq is called!\n";
	my ($hash_ref) = @_;
        my %patient_rnaseq_file_association = %$hash_ref;
    
		
	# initialize an empty set FULL_SET_OF_miRNAs
	my $FULL_SET_OF_ENSEMBL_IDS = Set::Object->new();
	my $FULL_SET_OF_GENE_NAME = Set::Object->new();
	my $test_counter = 0;
	my @ensembl_id = ();
	my @gene_name;
	my @gene_symbol;
	my $gene_symbol_ref;
	my $gene_symbol_m;

	# for each case id, get name of folder and methylation file	
	foreach my $key (keys %patient_rnaseq_file_association){
                $test_counter=$test_counter + 1;
                printLog "... for patient #$test_counter ($key) in file $patient_rnaseq_file_association{$key}.\n";
                
		# open file
		my $counter=0;

		open DATA, $patient_rnaseq_file_association{$key} or die "Cannot find file $patient_rnaseq_file_association{$key}";
		
		#printLog "\n ----for $patient_rnaseq_file_association{$key}----\n";
		
		while (my $row = <DATA>){
				
			my @lines = split ("\t", $row);
			$ensembl_id[$counter] = $lines[0];	
			
# 			open ANN, "mart_export_versions.txt" or die "Cannot find file";
# 						
# 					while (my $line = <ANN>){
# 						
# 						my $mart_ensembl_id;
# 						my $mart_gene_name;
# 						
# 						if ($.>1){
# 							
# 							my @rows = split ("\t", $line);
# 							$mart_ensembl_id = $rows[0];
# 							$mart_gene_name = $rows[1];
# 							chomp $mart_gene_name;
# 							chomp $ensembl_id[$counter];
# 							if ($ensembl_id[$counter] eq $mart_ensembl_id){
# 								
# 								$gene_symbol_m = $mart_gene_name;
# 								#printLog "Ensembl $mart_ensembl_id or $ensembl_id has gene name $mart_gene_name";
# 								push(@gene_name,$gene_symbol_m);
# 							
# 							}
# 						}
# 						
# 					}
# 					close ANN;
# 					
# 			#printLog "ensemble ids $ensembl_id[$counter]\n";

                        # Get symbol name from hashtable
                        $gene_symbol_m = $symbols_look_up{$ensembl_id[$counter]};
                        
                        # Update list of gene symbols
                        push(@gene_name,$gene_symbol_m);
			$counter++;
	
		}
		my $length = @ensembl_id;
	#	printLog "$length";
	
		$FULL_SET_OF_ENSEMBL_IDS -> insert (@ensembl_id);
		$FULL_SET_OF_GENE_NAME -> insert (@gene_name);
		close DATA;
		
		
		@ensembl_id = ();
		@gene_name = ();
	}


	
	#creation of a hash that has as a key the case id and value a hash that is returned from the exractMethylationVector subroutine
	my %patient_rnaseq_data_association;
	$test_counter=0; # Reset counter
	foreach my $key (keys %patient_rnaseq_file_association){
		#printLog "hey babe\n$patient_mirna_file_association{$key}\n";
		#my $gene_ensembl_ref;
		
                $test_counter=$test_counter + 1;
                printLog "Extracting RNAseq vector for patient #$test_counter ($key)... ";
		($patient_rnaseq_data_association{$key}, $gene_symbol_ref) = exractRNAseqVector($patient_rnaseq_file_association{$key},\$FULL_SET_OF_ENSEMBL_IDS, \$FULL_SET_OF_GENE_NAME);
                printLog "Extracting RNAseq vector for patient #$test_counter ($key)... Done.\n";
		
		@gene_symbol = @$gene_symbol_ref;
	#	printLog "@gene_symbol/n";
		
	}
	
	#printLog Dumper \%patient_data_association;

	return (\%patient_rnaseq_data_association,\@gene_symbol);
	

}



sub get_mirna_id_from_mirna_files{

	no warnings;
	
	printLog "sub get_mirna_id_from_mirna_files is called!\n";
	my ($hash_ref) = @_;
    my %patient_mirna_file_association = %$hash_ref;
		
	# initialize an empty set FULL_SET_OF_miRNAs
	my $FULL_SET_OF_miRNAs = Set::Object->new();
	my @miRNAs_id;

	# for each case id, get name of folder and methylation file	
	foreach my $key (keys %patient_mirna_file_association){
		# open file
		my $counter=0;
		open DATA, $patient_mirna_file_association{$key} or die "Cannot find file";
		
		while (my $row = <DATA>){
			
			if ($. > 1){
				
				my @lines = split ("\t", $row);
				$miRNAs_id[$counter] = $lines[0];		
				$counter++;
				
			}
		
		}
		$FULL_SET_OF_miRNAs -> insert (@miRNAs_id);
		
		close DATA;
		#printLog "mirna id @miRNAs_id\n";
		@miRNAs_id = ();
}
	#printLog " set $FULL_SET_OF_miRNAs\n";
	#creation of a hash that has as a key the case id and value a hash that is returned from the exractMethylationVector subroutine
	my $mirna_id_sorted_ref;
	my %patient_mirna_data_association;
	my @mirna_id_sorted;
	foreach my $patient (keys %patient_mirna_file_association){
	
		($patient_mirna_data_association{$patient},$mirna_id_sorted_ref )= exractmiRNAVector($patient_mirna_file_association{$patient},$FULL_SET_OF_miRNAs);

	}
	@mirna_id_sorted = @$mirna_id_sorted_ref;
	#printLog Dumper \%patient_mirna_file_association;
	return (\%patient_mirna_data_association, \@mirna_id_sorted);

}



sub get_gene_symbols_from_methylation_files{
	
	no warnings;
	
	printLog "meth sub is called!\n";
	my ($hash_ref) = @_;
        my %patient_methylation_file_association = %$hash_ref;
		
	# initialize an empty set FULL_SET_OF_GENE_NAMES
	my $FULL_SET_OF_GENE_NAMES = Set::Object->new();
	my @genes_per_probe = ();
	my @gene_symbol = ();
	
        printLog "Reading all methylation symbols...\n";
        my $test_counter = 0;
	# for each case id, get name of folder and methylation file
	foreach my $key (keys %patient_methylation_file_association){
		# open methylation file
		
		$test_counter = $test_counter + 1;
		printLog "Reading symbols for patient #$test_counter ($key) from file $patient_methylation_file_association{$key}...";
		open DATA, $patient_methylation_file_association{$key} or die "Cannot find file";
		#printLog "\n ----for $patient_methylation_file_association{$key}----\n";
		my $counter=0;
		
		while (my $row = <DATA>){
			
			if ($. > 1){
				
				my @lines = split ("\t", $row);
				$genes_per_probe[$counter] = $lines[5];		
				@gene_symbol = split (/;/, $genes_per_probe[$counter]);
				$counter++;
				$FULL_SET_OF_GENE_NAMES -> insert (@gene_symbol);
			}
		
		}
		
		
		
		#$FULL_SET_OF_GENE_NAMES -> insert (@gene_symbol);
		close DATA;
		printLog " Done.\n";

		@genes_per_probe = ();
		@gene_symbol = ();
	}
        printLog "Reading all methylation symbols... Done.\n ";

	# -- at this point we have the full list of genes- it works fine

	# init the case list METHYLATION_INFO_OF_CASES
	# for each id
		# get name of methylation file
		# exractMethylationVector(methylationFileName, FULL_SET_OF_GENE_NAMES) # Returns the methylation vector of the case with given ID, using the full list of genes extracted above
			# the methylation vector is a mapping between gene and methylation beta value
		# add returned vector to METHYLATION_INFO_OF_CASES
	# store the METHYLATION_INFO_OF_CASES to a file
	##################
	# for each id, get name of methylation file	
	#creation of a hash that has as a key the case id and value a hash that is returned from the exractMethylationVector subroutine
	my $gene_sorted_meth_ref;
	my %patient_meth_data_association;
	my @gene_sorted_meth;
	foreach my $patient (keys %patient_methylation_file_association){
            printLog "Extracting methylation vector for patient $patient in file $patient_methylation_file_association{$patient}...";		
		($patient_meth_data_association{$patient},$gene_sorted_meth_ref) = exractMethylationVector($patient_methylation_file_association{$patient},\$FULL_SET_OF_GENE_NAMES);
            printLog " Done.\n";
	}
	@gene_sorted_meth = @$gene_sorted_meth_ref;
	#printLog "Also Here!!!\n";
	#printLog Dumper \%patient_meth_data_association;
	return (\%patient_meth_data_association,\@gene_sorted_meth);

}



# function exractMethylationVector(filename, fullListOfGeneNames)
	# create a map RES_VEC containing all the gene names as keys and NA as values
	# open file
	# for every gene name I find in the file
		# read the corresponding value from the file
		# update the RES_VEC vector for the specific gene with the value we just read
	# return RES_VEC
	
sub exractRNAseqVector{

	no warnings;
# 	printLog "ExtractRNASeqVector parsing parameters...\n";
	my ($filename, $fullLSetOfEnsemblId_Ref, $fullLSetOfEntrezGeneId_Ref ) = @_;
	my $fullLSetOfEnsemblId = ${$fullLSetOfEnsemblId_Ref};
	my $fullLSetOfEntrezGeneId = ${$fullLSetOfEntrezGeneId_Ref};
#         printLog "ExtractRNASeqVector parsed parameters... Done\n";
	
# 	printLog "ExtractRNASeqVector extracting sizes...\n";
	my $sizeEnsembl = $fullLSetOfEnsemblId -> size();
	my $sizeEntrez = $fullLSetOfEntrezGeneId -> size();
# 	printLog "ExtractRNASeqVector extracting sizes... Done.\n";
	
	my $FPKM;
	my @column;
	my $miRNA_id;
	my @gene;
	my $counter;
	my $reads_counter = 0;
	my $ensembl_id;
	
	# create a hash RES_VEC containing all the gene names as keys and NA as values
	my %RES_VEC;
	
	#sort @set_ensembl and @set_gene_name alphabetically
# 	printLog "Sorting gene Ensembl hash...\n";
	my @set_ensembl = ${$fullLSetOfEnsemblId_Ref}->members();
	sort { (lc($a) cmp lc($b)) or ($a cmp $b) } @set_ensembl;
# 	printLog "Sorting gene Ensembl hash...Done. \n";
	
# 	printLog "Sorting gene Entrez ids...\n";
	my @set_gene_name = ${$fullLSetOfEntrezGeneId_Ref}->members();
	sort { (lc($a) cmp lc($b)) or ($a cmp $b) } @set_gene_name;
# 	printLog "Sorting gene Entrez ids... Done.\n";
	
# 	printLog "Initializing vector...\n";
	for ($counter=0; $counter < $sizeEntrez; $counter++){
		
		$RES_VEC{$set_gene_name[$counter]} = undef;
	
	}
# 	printLog "Initializing vector... Done.\n";

	#sygkrisi olwn twn genes pou exoun apothikeutei sto set me oles tis grammes gia ola ta arxeia
	#sygkrisi kathe gene sto @set_elements me oles tis grammes kathe arxeioy
	
# 	printLog "Extracting vector from file $filename...\n";
	
        open FILE, $filename or die "Cannot find file $filename\n";
        # For every line in the RNAseq file
        while (my $row = <FILE>){
            @column = split ("\t", $row);
    
            # Get gene ID
            $ensembl_id= $column[0];
            
            # If the set contains the ensemblID
            if ($fullLSetOfEnsemblId->member($ensembl_id)){
                # Get column 1 value and map it to the corresponding gene game (from the overall hashmap)
                my $geneSymbol = $symbols_look_up{$ensembl_id};
                chomp $column[1];
                $RES_VEC{$geneSymbol} = $column[1];
                
            }
			
            @column = ();
    
	}
        close FILE;
        
# 	printLog "Extracting vector from file $filename... Done.\n";
	
        #printLog Dumper \%RES_VEC;
        return (\%RES_VEC, \@set_gene_name);
}
	
	
sub exractmiRNAVector{

	no warnings;

	my ($filename, $fullLSetOfmiRNAs) = @_;
	my $size = $fullLSetOfmiRNAs -> size();
	my @set_elements =  @$fullLSetOfmiRNAs;
	my $reads_per_million_miRNA_mapped;
	my @column;
	my $miRNA_id;
	my @gene;
	my $counter;
	my $reads_counter = 0;
	printLog "Extracting miRNA Vector for $filename...\n";
	# create a hash RES_VEC containing all the gene names as keys and NA as values
	my %RES_VEC;
	
	sort { (lc($a) cmp lc($b)) or ($a cmp $b) } @set_elements;
	for ($counter=0; $counter < $size; $counter++){
		
		$RES_VEC{$set_elements[$counter]} = undef;

	}



	#sygkrisi olwn twn genes pou exoun apothikeutei sto set me oles tis grammes gia ola ta arxeia
	#sygkrisi kathe gene sto @set_elements me oles tis grammes kathe arxeioy
        # Open the patient miRNA file
        open FILE, $filename or die "Cannot find file $filename";
        
        # For every row beyond the first (header)
        while (my $row = <FILE>){
                if ($. > 1){
                    # Get the miRNA ID from the row
                    @column = split ("\t", $row);
                    $miRNA_id= $column[0];
                    chomp $miRNA_id;

                    # If the $RES_VEC contains the miRNA_id
                    if (exists $RES_VEC{$miRNA_id}){
                        # Update the value of the hash
                        $RES_VEC{$miRNA_id} = $column[2];
                    }
		
		  }
		@column = ();
	}
        #@reads_per_million_miRNA_mapped = ();
        #$reads_counter = 0;
        close FILE;
        #printLog Dumper \%RES_VEC;
	printLog "Extracting miRNA Vector for %filename... Done.\n";
        return (\%RES_VEC,\@set_elements);
}
	
sub exractMethylationVector{

	no warnings;
	#use warnings;
	
	my ($filename, $fullLSetOfGeneNames_Ref) = @_;
 	my $fullLSetOfGeneNames = ${$fullLSetOfGeneNames_Ref};
	my @set_elements =  $fullLSetOfGeneNames->members();
	my $size = scalar @set_elements;
	my @beta_value;
	my @column;
	my @genes_probe;
	my @gene;
	my $counter;
	my $beta_counter = 0;
	
	# create a hash beta_values containing all the gene names as keys and NA as values
	my %betaValues;
	
	sort { (lc($a) cmp lc($b)) or ($a cmp $b) } @set_elements;
	for ($counter=0; $counter < $size; $counter++){
		
		$betaValues{$set_elements[$counter]} = undef;
	
	}
	
	#open file
	
	#printLog "\n$filename\n";
	#sygkrisi olwn twn genes pou exoun apothikeutei sto set me oles tis grammes gia ola ta arxeia
	#sygkrisi kathe gene sto @set_elements me oles tis grammes kathe arxeioy
        open FILE, $filename or die "Cannot find file $filename";
        #printLog "\n heyyyy $set_elements[$counter]\n";
        
        my %betaGeneCount;
        
        # For every row $currentRow
        while (my $row = <FILE>){
            # If it contains something
            if ($. > 1){
                # Split the line
                @column = split ("\t", $row);
                # Get genes names from corresponding field
                my @rowGenes = split(";", $column[5]);
                                
                # Initialize set to keep visited genes in this row
                my $visitedGenesInRow = Set::Object->new();
                # For every gene $rowGene in the row
                foreach my $rowGene (@rowGenes) {
                    # If it refers to one of our genes $ourGene, and we have NOT already used $rowGene it in the current row
                    if ($fullLSetOfGeneNames->member($rowGene) and not $visitedGenesInRow->member($rowGene)) {
                            # Update the corresponding $rowGene average
                            # If this is the first time we update the gene
                            if ($betaValues{$rowGene} eq undef) {
                                # Set the value
                                $betaValues{$rowGene} = $column[1];
                                $betaGeneCount{$rowGene} = 1;
                            }
                            else {
                                my $curWeight = $betaGeneCount{$rowGene};
                                
                                # If the value is not NA then
                                if ($column[1] != 'N/A') {
                                    # Update the average
                                    $betaValues{$rowGene} = (($curWeight * $betaValues{$rowGene}) + $column[1]) / ($curWeight + 1);
                                    # Increase count of times we found the gene
                                    $betaGeneCount{$rowGene} = $betaGeneCount{$rowGene} + 1;
                                }
                            }
                            # Make sure we do not revisit this gene, in the current row
                            $visitedGenesInRow->insert($rowGene);
                    }
                }
            }
        }
        # Return the result
        return (\%betaValues,\@set_elements);

}



#osa exoun NA beta_value moy bgazei pws exei 0 average. to opoio einai lathos! na brw tropo na to diorthwsw!
#den thelw oi NA values na upologzontai sto average giati to 0 exei fysiki simasia
 sub average_beta_value_per_gene{

	no warnings;
	printLog "\naverage_beta_value_per_gene is called!\n";
	my ($ref_beta_value) = @_;
	my @beta_value_def = @{$ref_beta_value};
	#printLog "\n beta_value_def is @beta_value_def\n ";
	return unless @beta_value_def;
	my $length = @beta_value_def;
	#printLog "length $length\n";
	my $total;
	my $array_length; 
	my @beta_value_array = ();

	my $counter;
	for ( $counter =0; $counter < $length; $counter ++){
		
		if ($beta_value_def[$counter] != 'N/A'){
	
			$beta_value_array[$counter] = $beta_value_def[$counter];
			$array_length++;
		}
		
	}
	#printLog "\n beta_value_array is @beta_value_array\n ";
	if ($array_length == 0 ){
		
		return undef;
	
	}
	
	else{
		my $total =0;
		#my $array_length = @beta_value_array;
		#printLog "array_length is $array_length \n";
		#for ( $counter =0; $counter< $array_length; $counter++){
		#	printLog "\nhey\n";
		#	$total = $beta_value_array[$counter]+ $total; 
			
		#}
		foreach (@beta_value_array) {
		
				$total += $_;
			
			
	    }

		
		return $total/$array_length;
	
	}
	
}
